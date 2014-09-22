package net.squarelabs.sqorm.dataset;

import net.squarelabs.sqorm.fluent.Query;
import net.squarelabs.sqorm.index.BaseIndex;
import net.squarelabs.sqorm.index.Key;
import net.squarelabs.sqorm.index.MultiHashIndex;
import net.squarelabs.sqorm.schema.IndexSchema;
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

    public BaseIndex getPrimaryIndex() {
        return getIndex(table.getPrimaryKey());
    }

    public Key getPrimaryKey(Object record) {
        return getPrimaryIndex().getKey(record);
    }

    public Object findByPk(Key pk) {
        return getPrimaryIndex().findOne(pk);
    }

    @Override
    public boolean add(Object record) throws UnsupportedOperationException {
        Key pk = getPrimaryKey(record);
        Object existing = findByPk(pk);

        // Remove
        if(existing != null) {
            for(BaseIndex idx : indices.values()) {
                idx.remove(existing);
            }
            remove(existing);
        }

        // Add
        for(BaseIndex idx : indices.values()) {
            idx.add(record);
        }
        return super.add(record);
    }

    @Override
    public <T> T top() {
        return (T)iterator().next();
    }

    @Override
    public <T> Collection<T> all() {
        return (Collection<T>)this;
    }
}
