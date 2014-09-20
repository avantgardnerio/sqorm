package net.squarelabs.sqorm.codegen.model;

public class Column {

    private String columnName;
    private String tableName;
    private String dataType;
    private String defaultValue;
    private String ordinal;
    private String description;

    private String foreignTableName;
    private String foreignColumnName;

    private int seed;
    private int scale;
    private int increment;
    private int maxLength;
    private int precision;
    private boolean isNullable;

    private boolean isIdentity;
    private boolean isPrimaryKey;
    private boolean isArray;

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
    public int getMaxLength() {
        return maxLength;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "max_length")
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "precision")
    public int getPrecision() {
        return precision;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "precision")
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "scale")
    public int getScale() {
        return scale;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "scale")
    public void setScale(int scale) {
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
    public int getSeed() {
        return seed;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "seed")
    public void setSeed(int seed) {
        this.seed = seed;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "increment")
    public int getIncrement() {
        return increment;
    }

    @net.squarelabs.sqorm.annotation.Column(name = "increment")
    public void setIncrement(int increment) {
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

}
