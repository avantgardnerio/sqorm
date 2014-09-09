package net.squarelabs.sqorm.dataset;

import java.util.ArrayList;

public class Recordset extends ArrayList<Object> {

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
}
