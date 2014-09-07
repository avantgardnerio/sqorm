package net.squarelabs.sqorm.schema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseSchema {

    private final Map<Class, TableSchema> tables = new ConcurrentHashMap<>();

    public synchronized TableSchema ensureTable( Class<?> clazz ) {
        TableSchema table = tables.get( clazz );
        if ( table == null ) {
            table = new TableSchema( clazz );
            tables.put( clazz, table );
        }
        return table;
    }
}
