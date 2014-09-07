package net.squarelabs.sqorm.schema;

public class TableSchema {

    private final Class<?> clazz;

    public TableSchema(Class<?> clazz) {
        this.clazz = clazz;
    }
}
