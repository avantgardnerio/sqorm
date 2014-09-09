package net.squarelabs.sqorm;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QueryCache {

    private String queryFolder = "queries";
    private final Map<String, String> queries = new ConcurrentHashMap<>();

    public String loadQuery(String name) {
        String sql = queries.get(name);
        if(sql != null) {
            return sql;
        }
        synchronized (this) {
            String path = new File(queryFolder, name + ".sql").getPath();
            try(InputStream stream = QueryCache.class.getResourceAsStream(path)) {
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

}
