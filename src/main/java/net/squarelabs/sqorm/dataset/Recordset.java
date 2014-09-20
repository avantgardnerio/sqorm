package net.squarelabs.sqorm.dataset;

import net.squarelabs.sqorm.fluent.Query;
import net.squarelabs.sqorm.index.BaseIndex;
import net.squarelabs.sqorm.index.MultiHashIndex;
import net.squarelabs.sqorm.schema.IndexSchema;
import net.squarelabs.sqorm.schema.RelationSchema;
import net.squarelabs.sqorm.schema.TableSchema;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Recordset extends ArrayList<Object> implements Query {

    private final TableSchema table;
    private final Class<?> clazz;

    private final Map<IndexSchema,BaseIndex> indices = new ConcurrentHashMap<>();

    public Recordset(TableSchema table) {
        this.table = table;
        this.clazz = table.getType();
        createIndices();
    }

    public TableSchema getTable() {
        return table;
    }

    public BaseIndex getIndex(IndexSchema schema) {
        return indices.get(schema);
    }

    private void createIndices() {
        for(IndexSchema idx : table.getIndices()) {
            indices.put(idx, new MultiHashIndex(idx));
        }
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
