package net.squarelabs.sqorm.schema;

import net.squarelabs.sqorm.annotation.Table;
import net.squarelabs.sqorm.driver.DbDriver;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DbSchema {

    private final DbDriver driver;

    private final Map<Class<?>, TableSchema> tables = new ConcurrentHashMap<>();

    public DbSchema(DbDriver driver, String namespace) {
        this.driver = driver;

        // Build a list of all the tables
        Set<Class<?>> pojos = new Reflections(namespace).getTypesAnnotatedWith(Table.class);
        for(Class<?> clazz : pojos) {
            TableSchema table = new TableSchema(clazz, driver);
            tables.put(clazz, table);
        }

        // TODO: Build list of relationships
    }

    public TableSchema getTable(Class<?> clazz) {
        return tables.get(clazz);
    }
}
