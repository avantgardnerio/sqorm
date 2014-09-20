package net.squarelabs.sqorm.dataset;

import net.squarelabs.sqorm.fluent.Query;

import java.util.ArrayList;
import java.util.Collection;

public class Recordset extends ArrayList<Object> implements Query {

    private final Class<?> clazz;

    public Recordset(Class<?> clazz) {
        this.clazz = clazz;
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
