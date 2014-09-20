package net.squarelabs.sqorm.index;

import com.google.common.collect.HashMultimap;
import net.squarelabs.sqorm.schema.IndexSchema;

import java.util.Map;
import java.util.Set;

public class MultiHashIndex extends BaseIndex {

    private final HashMultimap<Key, Object> map = HashMultimap.create();

    public MultiHashIndex(IndexSchema idx) {
        super(idx);
    }

    @Override
    public Set<Object> find(Key key) {
        return map.get(key);
    }

    @Override
    public void add(Object record) {
        Key key = getKey(record);
        map.put(key, record);
    }

    @Override
    public void remove(Object record) {
        Key key = getKey(record);
        map.removeAll(key);
    }

}
