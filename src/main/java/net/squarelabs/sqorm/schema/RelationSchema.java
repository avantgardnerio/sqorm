package net.squarelabs.sqorm.schema;

import java.lang.reflect.Method;
import java.util.Collection;

public class RelationSchema {

    private final String name;
    private final TableSchema primaryTable;
    private final TableSchema foreignTable;
    private final IndexSchema primaryKey;
    private final IndexSchema foreignKey;
    private final Method parentSetter;
    private final Method childSetter;

    public RelationSchema(String name, TableSchema primaryTable, TableSchema foreignTable,
                          IndexSchema primaryKey, IndexSchema foreignKey,
                          Method parentSetter, Method childSetter) {
        this.name = name;
        this.primaryTable = primaryTable;
        this.foreignTable = foreignTable;
        this.primaryKey = primaryKey;
        this.foreignKey = foreignKey;
        this.parentSetter = parentSetter;
        this.childSetter = childSetter;
    }

    public TableSchema getForeignTable() {
        return foreignTable;
    }

    public TableSchema getPrimaryTable() {
        return primaryTable;
    }

    public IndexSchema getPrimaryIndex() {
        return primaryKey;
    }

    public IndexSchema getForeignIndex() {
        return foreignKey;
    }

    public void setParent(Object child, Object parent) {
        try {
            parentSetter.invoke(child, parent);
        } catch (Exception ex) {
            throw new RuntimeException("Error setting parent!", ex);
        }
    }

    public void setChildren(Object parent, Collection<?> children) {
        try {
            childSetter.invoke(parent, children);
        } catch (Exception ex) {
            throw new RuntimeException("Error setting parent!", ex);
        }
    }
}
