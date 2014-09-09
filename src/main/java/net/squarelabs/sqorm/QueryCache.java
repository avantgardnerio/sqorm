package net.squarelabs.sqorm;

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
import java.util.stream.Collectors;

public class QueryCache {

    private String queryFolder = "queries";
    private final Map<String, MultiQuery> queries = new ConcurrentHashMap<>();

    private final DbDriver driver;

    public QueryCache(DbDriver driver) {
        this.driver = driver;
    }

    public PreparedStatement prepareStatement(Connection con, Query query, Map<String,Object> parms)
            throws SQLException {
        PreparedStatement stmt = con.prepareStatement(query.getSql());
        try {
            int i = 1;
            for(String parmName : query.getParameters()) {
                if(!parms.containsKey(parmName)) {
                    throw new RuntimeException("Value not specified for parameter: " + parmName);
                }
                Object val = parms.get(parmName);
                stmt.setObject(i, val);
            }
        } catch (Exception ex) {
            stmt.close();
            throw ex;
        }
        return stmt;
    }

    public List<Query> loadQuery(String name) {
        MultiQuery query = queries.get(name);
        if (query != null) {
            return query;
        }
        synchronized (this) {
            String path = "/" + new File(queryFolder, name + ".sql").getPath();
            try (InputStream stream = QueryCache.class.getClass().getResourceAsStream(path)) {
                if (stream == null) {
                    throw new InvalidParameterException("Query not found: " + path);
                }
                String sql = IOUtils.toString(stream, "UTF-8");
                List<String> statements = splitQuery(sql, driver.mars());
                query = new MultiQuery();
                for(String str : statements) {
                    Query q = loadStatement(str);
                    query.add(q);
                }
                queries.put(name, query);
                return query;
            } catch (Exception ex) {
                throw new RuntimeException("Error loading query!", ex);
            }
        }
    }

    public String getQueryFolder() {
        return queryFolder;
    }

    public void setQueryFolder(String queryFolder) {
        this.queryFolder = queryFolder;
    }

    /**
     * Parse a SQL statement for named parameters
     *
     * @param sql The SQL statement to parse
     * @return A Query object containing the modified SQL and a list of parameters
     */
    public static Query loadStatement(String sql) {
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
            }

            // Build string
            sb.append(chr);
        }
        Query query = new Query(sb.toString(), parms.toArray(new String[]{}));
        return query;
    }

    /**
     * Split SQL into multiple statements if the DB does not support mars
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
