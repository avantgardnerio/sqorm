package net.squarelabs.sqorm.codegen.model;

import net.squarelabs.sqorm.annotation.Association;

import java.util.List;

@net.squarelabs.sqorm.annotation.Table(name = "Column")
public class Column {

    private String columnName;
    private String tableName;
    private String dataType;
    private String defaultValue;
    private String ordinal;
    private String description;

    private String foreignTableName;
    private String foreignColumnName;

    private Integer seed;
    private Integer scale;
    private Integer increment;
    private Integer maxLength;
    private Integer precision;
    private boolean isNullable;

    private boolean isIdentity;
    private boolean isPrimaryKey;
    private boolean isArray;

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

    @net.squarelabs.sqorm.annotation.Column(name = "identity")
    public boolean isIdentity() {
        return isIdentity;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "identity")
    public void setIdentity(boolean isIdentity) {
        this.isIdentity = isIdentity;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "data_type")
    public String getDataType() {
        return dataType;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "data_type")
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "max_length")
    public Integer getMaxLength() {
        return maxLength;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "max_length")
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "precision")
    public Integer getPrecision() {
        return precision;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "precision")
    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "scale")
    public Integer getScale() {
        return scale;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "scale")
    public void setScale(Integer scale) {
        this.scale = scale;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "nullable")
    public boolean isNullable() {
        return isNullable;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "nullable")
    public void setNullable(boolean isNullable) {
        this.isNullable = isNullable;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "default_value")
    public String getDefaultValue() {
        return defaultValue;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "default_value")
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "seed")
    public Integer getSeed() {
        return seed;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "seed")
    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "increment")
    public Integer getIncrement() {
        return increment;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "increment")
    public void setIncrement(Integer increment) {
        this.increment = increment;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "primary_key")
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "primary_key")
    public void setPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "is_array")
    public boolean isArray() {
        return isArray;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "is_array")
    public void setArray(boolean isArray) {
        this.isArray = isArray;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "ordinal")
    public String getOrdinal() {
        return ordinal;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "ordinal")
    public void setOrdinal(String ordinal) {
        this.ordinal = ordinal;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "description")
    public String getDescription() {
        return description;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "description")
    public void setDescription(String description) {
        this.description = description;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "foreign_table")
    public String getForeignTableName() {
        return foreignTableName;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "foreign_table")
    public void setForeignTableName(String foreignTableName) {
        this.foreignTableName = foreignTableName;
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

    @Association(name = "TableColumnRel", primaryKey = "table_name", foreignKey = "table_name", isForeignKey = true)
    public Table getTableParent() {
        return parentTable;
    }

    @Association(name = "TableColumnRel", primaryKey = "table_name", foreignKey = "table_name", isForeignKey = true)
    public void setTableParent(Table val) {
        parentTable = val;
    }

}
