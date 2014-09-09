package net.squarelabs.sqorm.schema;

import net.squarelabs.sqorm.driver.DbDriver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DbSchema {

    private final DbDriver driver;

    private final Map<Class<?>, TableSchema> tables = new ConcurrentHashMap<>();

    public DbSchema(DbDriver driver) {
        this.driver = driver;
    }

    public TableSchema ensureTable(Class<?> clazz) {
        TableSchema table = tables.get(clazz);
        if (table != null) {
            return table;
        }
        synchronized (this) {
            table = new TableSchema(clazz, driver);
            tables.put(clazz, table);
            return table;
        }
    }
}
