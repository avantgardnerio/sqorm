package net.squarelabs.sqorm.codegen.model;

        import net.squarelabs.sqorm.annotation.Association;

        import java.util.ArrayList;
        import java.util.Collection;
        import java.util.List;

@net.squarelabs.sqorm.annotation.Table(name = "Table")
public class Table {

    private String name;
    private String description;

    private Collection<Column> columnChildren = new ArrayList<>();

    private Collection<Relation> parentRelations = new ArrayList<>();
    private Collection<Relation> childRelations = new ArrayList<>();

    @net.squarelabs.sqorm.annotation.Column(name="table_name", pkOrdinal = 0)
    public String getName() {
        return name;
    }

    @net.squarelabs.sqorm.annotation.Column(name="table_name", pkOrdinal = 0)
    public void setName(String name) {
        this.name = name;
    }

    @net.squarelabs.sqorm.annotation.Column(name="description")
    public void setDescription(String description) {
        this.description = description;
    }

    @net.squarelabs.sqorm.annotation.Column(name="description")

    @Association(name = "TableColumnRel", primaryKey = "name", foreignKey = "name", isForeignKey = false)
    public String getDescription() {
        return description;
    }

    // ------------------------------------- Child Associations -------------------------------------------------------

    @Association(name = "TableColumnRel", primaryKey = "table_name", foreignKey = "table_name", isForeignKey = false)
    public Collection<Column> getColumnChildren() {
        return columnChildren;
    }

    @Association(name = "TableColumnRel", primaryKey = "table_name", foreignKey = "table_name", isForeignKey = false)
    public void setColumnChildren(Collection<Column> val) {
        columnChildren = val;
    }

    @Association(name = "TableParentRel", primaryKey = "table_name", foreignKey = "primary_table", isForeignKey = false)
    public Collection<Relation> getParentRelations() {
        return parentRelations;
    }

    @Association(name = "TableParentRel", primaryKey = "table_name", foreignKey = "primary_table", isForeignKey = false)
    public void setParentRelations(Collection<Relation> val) {
        parentRelations = val;
    }

    @Association(name = "TableChildRel", primaryKey = "table_name", foreignKey = "foreign_table", isForeignKey = false)
    public Collection<Relation> getChildRelations() {
        return childRelations;
    }

    @Association(name = "TableChildRel", primaryKey = "table_name", foreignKey = "foreign_table", isForeignKey = false)
    public void setChildRelations(Collection<Relation> val) {
        childRelations = val;
    }

}
