package net.squarelabs.sqorm.codegen.model;

import net.squarelabs.sqorm.annotation.Association;

import java.util.Collection;

@net.squarelabs.sqorm.annotation.Table(name = "RelationField")
public class RelationField {

    private String relationName;
    private int ordinalPosition;
    private String primaryColumnName;
    private String foreignColumnName;

    private Relation relationParent;

    @net.squarelabs.sqorm.annotation.Column(name = "constraint_name", pkOrdinal = 0)
    public String getRelationName() {
        return relationName;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "constraint_name", pkOrdinal = 0)
    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "ordinal_position", pkOrdinal = 1)
    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "ordinal_position", pkOrdinal = 1)
    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "primary_column")
    public String getPrimaryColumnName() {
        return primaryColumnName;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "primary_column")
    public void setPrimaryColumnName(String primaryColumnName) {
        this.primaryColumnName = primaryColumnName;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "foreign_column")
    public String getForeignColumnName() {
        return foreignColumnName;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "foreign_column")
    public void setForeignColumnName(String foreignColumnName) {
        this.foreignColumnName = foreignColumnName;
    }

    // ------------------------------------- Parent Associations ------------------------------------------------------

    @Association(name = "RelationFieldRel", primaryKey = "constraint_name", foreignKey = "constraint_name", isForeignKey = true)
    public Relation getRelationParent() {
        return relationParent;
    }

    @Association(name = "RelationFieldRel", primaryKey = "constraint_name", foreignKey = "constraint_name", isForeignKey = true)
    public void setRelationParent(Relation relationParent) {
        this.relationParent = relationParent;
    }

}
