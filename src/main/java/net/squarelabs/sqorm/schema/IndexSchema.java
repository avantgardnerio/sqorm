package net.squarelabs.sqorm.schema;

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

    public Object[] getKey(Object record) {
        Object[] key = new Object[columns.size()];
        for(int i = 0; i < key.length; i++) {
            ColumnSchema col = columns.get(i);
            key[i] = col.get(record);
        }
        return key;
    }
}
