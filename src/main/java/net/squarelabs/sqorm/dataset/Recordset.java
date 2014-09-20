package net.squarelabs.sqorm.dataset;

import net.squarelabs.sqorm.fluent.Query;
import net.squarelabs.sqorm.schema.TableSchema;

import java.util.ArrayList;
import java.util.Collection;

public class Recordset extends ArrayList<Object> implements Query {

    private final TableSchema table;
    private final Class<?> clazz;

    public Recordset(TableSchema table) {
        this.table = table;
        this.clazz = table.getType();
    }

    public Class<?> recordClass() {
        return clazz;
    }

    @Override
    public boolean add(Object record) throws UnsupportedOperationException {
        // TODO: Check for existing record with same PK, and remove it first!
        return super.add(record);
    }

    @Override
    public <T> Collection<T> all() {
        return (Collection<T>)this;
    }
}
