package net.squarelabs.sqorm.sql;

import net.squarelabs.sqorm.driver.DbDriver;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class QueryCache {

    private String queryFolder = "queries";
    private final Map<String, MultiQuery> queries = new ConcurrentHashMap<>();

    private final DbDriver driver;

    public QueryCache(DbDriver driver) {
        this.driver = driver;
    }

    public QueryCache(DbDriver driver, String queryFolder) {
        this.driver = driver;
        this.queryFolder = queryFolder;
    }

    public String getQueryFolder() {
        return queryFolder;
    }

    public void setQueryFolder(String queryFolder) {
        this.queryFolder = queryFolder;
    }

    /**
     * Turns a Query into a PreparedStatement ready for execution (Remember to close the resulting statement!)
     *
     * @param con The connection on which to create the PreparedStatement
     * @param query The Query that will be converted
     * @param parms A map of named parameters and values to pass to the PreparedStatement
     * @return A PreparedStatement object ready for execution against the DB
     * @throws SQLException
     */
    public PreparedStatement prepareStatement(Connection con, SqlQuery query, Map<String,Object> parms)
            throws SQLException {
        PreparedStatement stmt = con.prepareStatement(query.getSql());
        try {
            int i = 1;
            for(String parmName : query.getParameters()) {
                if(!parms.containsKey(parmName)) {
                    throw new RuntimeException("Value not specified for parameter: " + parmName);
                }
                Object val = parms.get(parmName);
                stmt.setObject(i++, val);
            }
        } catch (Exception ex) {
            stmt.close();
            throw ex;
        }
        return stmt;
    }

    /**
     * Turns a MultiQuery into a MultiStatement, populating it with named parameter values from a map
     *
     * @param con The connection on which to create the PreparedStatement
     * @param query The name of the query to load
     * @param parms A map of named parameters and values to pass to the PreparedStatement
     * @return A MultiStatement object ready for execution against the DB
     * @throws SQLException
     */
    public MultiStatement prepareStatements(Connection con, MultiQuery query, Map<String,Object> parms)
            throws SQLException {
        PreparedStatement[] stmts = new PreparedStatement[query.size()];
        for(int i = 0; i < stmts.length; i++) {
            SqlQuery q = query.get(i);
            stmts[i] = prepareStatement(con, q, parms);
        }
        MultiStatement stmt = new MultiStatement(stmts);
        return stmt;
    }

    /**
     * Create a PreparedStatement from the resource file with the given name, and populate it with parameters
     *
     * @param con The connection on which to create the PreparedStatement
     * @param name The name of the query to load
     * @param parms A map of named parameters and values to pass to the PreparedStatement
     * @return A MultiStatement object ready for execution against the DB
     */
    public MultiStatement prepareQuery(Connection con, String name, Map<String, Object> parms)
            throws SQLException {
        MultiQuery query = loadQuery(name);
        MultiStatement stmt = prepareStatements(con, query, parms);
        return stmt;
    }

    /**
     * Load a query resource file, break it up into multiple parts if necessary, translate it to the syntax for the
     * current database, and return it.
     *
     * @param name The name of the query to load
     * @return A Query object ready to be given parameters and turned into one or more PreparedStatements
     */
    public MultiQuery loadQuery(String name) {
        MultiQuery query = queries.get(name);
        if (query != null) {
            return query;
        }
        synchronized (this) {
            try (InputStream stream = getQuery(name)) {
                if (stream == null) {
                    throw new InvalidParameterException("Query not found: " + name);
                }
                String sql = IOUtils.toString(stream, "UTF-8");
                List<String> statements = splitQuery(sql, driver.mars());
                query = new MultiQuery();
                for(String str : statements) {
                    SqlQuery q = loadStatement(str, driver);
                    query.add(q);
                }
                queries.put(name, query);
                return query;
            } catch (Exception ex) {
                throw new RuntimeException("Error loading query!", ex);
            }
        }
    }

    /**
     * Gets a resource stream by name - be sure to close the result!
     *
     * @param name The resource stresm to get
     * @return A resource stream or null
     */
    private InputStream getQuery(String name) {
        File root = new File(queryFolder);

        // Try a DB-specific query
        File folder = new File(root, driver.name());
        File file = new File(folder, name + ".sql");
        String path = File.separator + file.getPath();
        InputStream stream = getClass().getResourceAsStream(path.replace("\\", "/"));
        if(stream != null) {
            return stream;
        }

        // Try a universal query
        file = new File(root, name + ".sql");
        path = File.separator + file.getPath();
        stream = getClass().getResourceAsStream(path.replace("\\", "/"));
        if(stream != null) {
            return stream;
        }

        return null;
    }

    /**
     * Parse a SQL statement for named parameters
     *
     * @param sql The SQL statement to parse
     * @return A Query object containing the modified SQL and a list of parameters
     */
    public static SqlQuery loadStatement(String sql, DbDriver driver) {
        sql = sql.trim();
        if(!sql.endsWith(";")) {
            sql += ";";
        }
        List<String> parms = new ArrayList<>();
        Stack<Character> tokenStack = new Stack<>();
        tokenStack.push('\0'); // Avoid empty stack exception
        char[] chars = sql.toCharArray();
        StringBuilder curTok = null;
        StringBuilder sb = new StringBuilder(chars.length);
        for (int i = 0; i < chars.length; i++) {
            char chr = chars[i];

            // Escape sequences
            if (tokenStack.peek() == '\\') {
                tokenStack.pop(); // The char right after '\' is a literal, we're now out of the escape sequence
            } else if (chr == '\\') {
                tokenStack.push(chr); // We've just entered the literal escape sequence
            } else if (chr == '\'') {
                if (tokenStack.peek() == '\'') {
                    tokenStack.pop();
                } else {
                    tokenStack.push('\'');
                }
            } else if (chr == '@' && tokenStack.size() == 1) {
                curTok = new StringBuilder();
                tokenStack.push(chr);
                chr = '?'; // Replace @NamedParameters with ? symbols
            } else if (tokenStack.peek() == '@') {
                if (isValidToken(chr)) {
                    curTok.append(chr);
                    continue;
                } else {
                    parms.add(curTok.toString());
                    curTok = null;
                    tokenStack.pop();
                }
            } else if(chr == '[') {
                chr = driver.se();
            } else if(chr == ']') {
                chr = driver.ee();
            }

            // Build string
            sb.append(chr);
        }
        SqlQuery query = new SqlQuery(sb.toString(), parms.toArray(new String[]{}));
        return query;
    }

    /**
     * Split SQL into multiple statements if the DB does not support mars
     *
     * @param sql The SQL to split
     * @param mars A flag indicating if the DB supports mars
     * @return A list of individual SQL statements
     */
    public static List<String> splitQuery(String sql, boolean mars) {
        if (mars) {
            return Arrays.asList(sql);
        }
        return splitQuery(sql);
    }

    /**
     * Split SQL into multiple statements based on semi-colons
     *
     * @param sql The SQL to split
     * @return A list of individual statements
     */
    public static List<String> splitQuery(String sql) {
        List<String> queries = new ArrayList<>();
        Stack<Character> tokenStack = new Stack<>();
        tokenStack.push('\0'); // Avoid empty stack exception
        char[] chars = sql.toCharArray();
        StringBuilder sb = new StringBuilder(chars.length);
        for (int i = 0; i < chars.length; i++) {
            char chr = chars[i];

            // Escape sequences
            if (tokenStack.peek() == '\\') {
                tokenStack.pop(); // The char right after '\' is a literal, we're now out of the escape sequence
            } else if (chr == '\\') {
                tokenStack.push(chr); // We've just entered the literal escape sequence
            } else if (chr == '\'') {
                if (tokenStack.peek() == '\'') {
                    tokenStack.pop();
                } else {
                    tokenStack.push('\'');
                }
            }

            // Build string
            sb.append(chr);
            if (tokenStack.size() <= 1 && chr == ';') {
                queries.add(sb.toString().trim());
                sb = new StringBuilder(chars.length - i);
            }
        }
        return queries;
    }

    /**
     * Function to determine if characters are valid in a named parameter
     *
     * @param chr The char to check
     * @return A flag indicating if the char can be used in a named parameter
     */
    private static boolean isValidToken(char chr) {
        if (chr >= 'a' && chr <= 'z') {
            return true;
        }
        if (chr >= 'A' && chr <= 'Z') {
            return true;
        }
        if (chr == '_') {
            return true;
        }
        return false;
    }

}
