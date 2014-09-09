package net.squarelabs.sqorm;

import net.squarelabs.sqorm.schema.ColumnSchema;
import net.squarelabs.sqorm.schema.DbSchema;
import net.squarelabs.sqorm.schema.TableSchema;

import java.sql.*;
import java.util.Iterator;

public class SqormCursor implements Iterable<Object>, Iterator<Object> {

    private DbSchema db;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;

    public SqormCursor(DbSchema db, PreparedStatement stmt) throws SQLException {
        this.db = db;
        this.resultSet = stmt.getResultSet();
        this.metaData = resultSet.getMetaData();
    }

    @Override
    public Object next() {
        try {
            if (!resultSet.next()) {
                return null;
            }
            String className = resultSet.getString(1);
            Class<?> clazz = Class.forName(className);
            TableSchema table = db.ensureTable(clazz);
            Object record = clazz.newInstance();
            int colCount = metaData.getColumnCount();
            for (int colIndex = 2; colIndex <= colCount; colIndex++) {
                Object val = resultSet.getObject(colIndex);
                String colName = metaData.getColumnName(colIndex);
                ColumnSchema col = table.getColumn(colName);
                col.set(record, val);
            }
            return record;
        } catch (Exception ex) {
            throw new RuntimeException("Error during cursor iteration!", ex);
        }
    }

    @Override
    public boolean hasNext() {
        try {
            return !resultSet.isLast() && !resultSet.isAfterLast();
        } catch (SQLException ex) {
            throw new RuntimeException("Error in cursor!", ex);
        }
    }

    @Override
    public Iterator<Object> iterator() {
        return this;
    }
}
