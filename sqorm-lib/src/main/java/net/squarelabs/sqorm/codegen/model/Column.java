package net.squarelabs.sqorm.codegen.model;

import net.squarelabs.sqorm.annotation.Association;

import java.util.List;

@net.squarelabs.sqorm.annotation.Table(name = "Column")
public class Column {

    private String columnName;
    private String tableName;
    private String dataType;

    private boolean autoIncrement;

    private Integer pkOrdinal;

    private Table parentTable;

    @net.squarelabs.sqorm.annotation.Column(name = "table_name", pkOrdinal = 0)
    public String getTableName() {
        return tableName;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "table_name", pkOrdinal = 0)
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "column_name", pkOrdinal = 1)
    public String getName() {
        return columnName;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "column_name", pkOrdinal = 1)
    public void setName(String name) {
        this.columnName = name;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "data_type")
    public String getDataType() {
        return dataType;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "data_type")
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "auto_increment")
    public boolean getAutoIncrement() {
        return autoIncrement;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "auto_increment")
    public void setAutoIncrement(boolean value) {
        this.autoIncrement = value;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "pk_ordinal")
    public Integer getPkOrdinal() {
        return pkOrdinal;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "pk_ordinal")
    public void setPkOrdinal(Integer pkOrdinal) {
        this.pkOrdinal = pkOrdinal;
    }

    // ------------------------------------- Parent Associations ------------------------------------------------------

    @Association(name = "TableColumnRel", primaryKey = "table_name", foreignKey = "table_name", isForeignKey = true)
    public Table getTableParent() {
        return parentTable;
    }

    @Association(name = "TableColumnRel", primaryKey = "table_name", foreignKey = "table_name", isForeignKey = true)
    public void setTableParent(Table val) {
        parentTable = val;
    }

}
