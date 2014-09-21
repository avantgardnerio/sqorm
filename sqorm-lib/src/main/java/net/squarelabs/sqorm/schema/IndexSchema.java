package net.squarelabs.sqorm.schema;

import net.squarelabs.sqorm.index.Key;

import java.util.List;

public class IndexSchema {

    private final List<ColumnSchema> columns;

    public IndexSchema(List<ColumnSchema> columns) {
        this.columns = columns;
    }

    public boolean matches(List<ColumnSchema> cols) {
        if(columns.size() != cols.size()) {
            return false;
        }
        for(int i = 0; i < columns.size(); i++) {
            if(!columns.get(i).equals(cols.get(i))) {
                return false;
            }
        }
        return true;
    }

    public Key getKey(Object record) {
        Key key = new Key();
        for(ColumnSchema col : columns) {
            key.add(col.get(record));
        }
        return key;
    }
}
