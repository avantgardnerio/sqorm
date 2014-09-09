package net.squarelabs.sqorm.schema;

import net.squarelabs.sqorm.annotation.Column;
import net.squarelabs.sqorm.annotation.Table;
import net.squarelabs.sqorm.driver.DbDriver;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

public class TableSchema {

    private final String tableName;

    // Reflection cache
    private final Map<String, ColumnSchema> columns;

    // Cached query syntax
    private final String insertQuery;

    public TableSchema(Class<?> clazz, DbDriver driver) {
        Table tableAno = clazz.getAnnotation(Table.class);
        if (tableAno == null) {
            throw new IllegalArgumentException("Class is not marked with Table annotation!");
        }
        tableName = tableAno.name();

        // Collect accessors
        columns = parseAnnotations(clazz);

        // Write queries
        insertQuery = writeInsertQuery(columns, driver);
    }

    private String writeInsertQuery(Map<String, ColumnSchema> columns, DbDriver driver) {
        String[] values = StringUtils.repeat("?", columns.size()).split("");
        String valueClause = StringUtils.join(values, ",");
        String delim = driver.ee() + "," + driver.se();
        String insertClause = driver.se() + StringUtils.join(columns.keySet(), delim) + driver.ee();
        String sql = String.format("insert into %s%s%s (%s) values (%s);",
                driver.se(), tableName, driver.ee(), insertClause, valueClause);
        return sql;
    }

    private Map<String, ColumnSchema> parseAnnotations(Class<?> clazz) {
        // Collect accessors
        Map<String, Method> getters = new HashMap<>();
        Map<String, Method> setters = new HashMap<>();
        for (Method method : clazz.getMethods()) {
            Column ano = method.getAnnotation(Column.class);
            if (ano == null) {
                continue;
            }
            String colName = ano.name();
            if (method.getReturnType() == void.class) {
                setters.put(colName, method);
            } else {
                getters.put(colName, method);
            }
        }

        // Translate to columns
        Map<String, ColumnSchema> columns = new HashMap<>();
        for(Map.Entry<String, Method> entry : getters.entrySet()) {
            String colName = entry.getKey();
            Method getter = entry.getValue();
            Method setter = setters.get(colName);
            ColumnSchema col = new ColumnSchema(colName, getter, setter);
            columns.put(colName, col);
        }
        return columns;
    }

    public void insert(Connection con, Object record) {
        try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
            int i = 1;
            for (Map.Entry<String, ColumnSchema> entry : columns.entrySet()) {
                ColumnSchema col = entry.getValue();
                Object val = col.get(record);
                stmt.setObject(i++, val);
            }
            int rowCount = stmt.executeUpdate();
            if (rowCount != 1) {
                throw new Exception("Insert failed!");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error inserting record!", ex);
        }
    }
}
