package net.squarelabs.sqorm.schema;

import java.util.List;

public class IndexSchema {

    private final TableSchema table;

    private final List<ColumnSchema> columns;

    public IndexSchema(TableSchema table, List<ColumnSchema> columns) {
        this.table = table;
        this.columns = columns;
    }
}
