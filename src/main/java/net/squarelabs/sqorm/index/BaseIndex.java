package net.squarelabs.sqorm.index;

import net.squarelabs.sqorm.schema.IndexSchema;

import java.util.Set;

public abstract class BaseIndex {

    private final IndexSchema idx;

    public BaseIndex(IndexSchema idx) {
        this.idx = idx;
    }

    public Object[] getKey(Object record) {
        return idx.getKey(record);
    }

    public abstract Set<Object> find(Object[] key);

    public abstract void add(Object record);

    public abstract void remove(Object record);

    // TODO: Fix this hack! Keep separate lists for single vs multi indexes?
    public Object findOne(Object[] key) {
        Set<Object> res = find(key);
        if(res.size() == 0) {
            return null;
        }
        if(res.size() > 1) {
            throw new RuntimeException("Unique index corrupted!");
        }
        return res.iterator().next();
    }
}
