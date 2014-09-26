package net.squarelabs.sqorm.codegen.model;

import net.squarelabs.sqorm.annotation.Association;

import java.util.ArrayList;
import java.util.Collection;

@net.squarelabs.sqorm.annotation.Table(name = "Relation")
public class Relation {

    private String name;
    private String primaryTableName;
    private String foreignTableName;

    private Collection<RelationField> fieldChildren = new ArrayList<>();

    @net.squarelabs.sqorm.annotation.Column(name = "constraint_name", pkOrdinal = 0)
    public String getName() {
        return name;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "constraint_name", pkOrdinal = 0)
    public void setName(String name) {
        this.name = name;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "primary_table")
    public String getPrimaryTableName() {
        return primaryTableName;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "primary_table")
    public void setPrimaryTableName(String primaryTableName) {
        this.primaryTableName = primaryTableName;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "foreign_table")
    public String getForeignTableName() {
        return foreignTableName;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "foreign_table")
    public void setForeignTableName(String foreignTableName) {
        this.foreignTableName = foreignTableName;
    }

    // ------------------------------------- Child Associations -------------------------------------------------------

    @Association(name = "RelationFieldRel", primaryKey = "constraint_name", foreignKey = "constraint_name", isForeignKey = false)
    public Collection<RelationField> getColumnChildren() {
        return fieldChildren;
    }

    @Association(name = "RelationFieldRel", primaryKey = "constraint_name", foreignKey = "constraint_name", isForeignKey = false)
    public void setColumnChildren(Collection<RelationField> val) {
        fieldChildren = val;
    }

}
