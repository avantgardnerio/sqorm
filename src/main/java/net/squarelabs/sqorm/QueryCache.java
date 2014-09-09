package net.squarelabs.sqorm;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class QueryCache {

    private String queryFolder = "queries";
    private final Map<String, String> queries = new ConcurrentHashMap<>();

    private final char[] tokens = new char [] {'\'', '\\'};

    public String loadQuery(String name) {
        String sql = queries.get(name);
        if(sql != null) {
            return sql;
        }
        synchronized (this) {
            String path = "/" + new File(queryFolder, name + ".sql").getPath();
            try(InputStream stream = QueryCache.class.getClass().getResourceAsStream(path)) {
                if(stream == null) {
                    throw new InvalidParameterException("Query not found: " + path);
                }
                sql = IOUtils.toString(stream, "UTF-8");
                queries.put(name, sql);
                return sql;
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

    public static List<String> splitQuery(String sql, boolean mars) {
        List<String> queries = new ArrayList<>();
        if(mars) {
            queries.add(sql);
            return queries;
        }
        Stack<Character> tokenStack = new Stack<>();
        tokenStack.push('\0'); // Avoid empty stack exception
        char[] chars = sql.toCharArray();
        StringBuilder sb = new StringBuilder(chars.length);
        for(int i = 0; i < chars.length; i++) {
            char chr = chars[i];

            // Escape sequences
            if(tokenStack.peek() == '\\') {
                tokenStack.pop(); // The char right after '\' is a literal, we're now out of the escape sequence
            } else if(chr == '\\') {
                tokenStack.push(chr); // We've just entered the literal escape sequence
            } else if(chr == '\'') {
                if(tokenStack.peek() == '\'') {
                    tokenStack.pop();
                } else {
                    tokenStack.push('\'');
                }
            }

            // Build string
            sb.append(chr);
            if(tokenStack.size() <= 1 && chr == ';') {
                queries.add(sb.toString().trim());
                sb = new StringBuilder(chars.length - i);
            }
        }
        return queries;
    }

}
