package net.squarelabs.sqorm.index;

import com.google.common.collect.HashMultimap;
import net.squarelabs.sqorm.schema.IndexSchema;

import java.util.Set;

public class MultiHashIndex extends BaseIndex {

    private final HashMultimap<Object[], Object> map = HashMultimap.create();

    public MultiHashIndex(IndexSchema idx) {
        super(idx);
    }

    @Override
    public Set<Object> find(Object[] key) {
        return map.get(key);
    }

}
