package net.squarelabs.sqorm.schema;

public class RelationSchema {

    private final String name;
    private final IndexSchema primaryKey;
    private final IndexSchema foreignKey;

    public RelationSchema(String name, IndexSchema primaryKey, IndexSchema foreignKey) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.foreignKey = foreignKey;
    }
}
