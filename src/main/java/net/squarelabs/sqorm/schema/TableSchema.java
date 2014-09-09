package net.squarelabs.sqorm.schema;

import net.squarelabs.sqorm.annotation.Column;
import net.squarelabs.sqorm.annotation.Table;
import net.squarelabs.sqorm.driver.DbDriver;
import org.apache.commons.lang.StringUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TableSchema {

    private final String tableName;

    // Reflection cache
    private final Map<String, ColumnSchema> columns;
    private final ColumnSchema versionCol;
    private final List<ColumnSchema> idColumns;

    // Cached query syntax
    private final String insertQuery;
    private final String updateQuery;

    public TableSchema(Class<?> clazz, DbDriver driver) {
        Table tableAno = clazz.getAnnotation(Table.class);
        if (tableAno == null) {
            throw new IllegalArgumentException("Class is not marked with Table annotation!");
        }
        tableName = tableAno.name();

        // Collect accessors
        columns = parseAnnotations(clazz);
        versionCol = findVersionCol(columns);
        idColumns = findIdCols(columns);

        // Write queries
        insertQuery = writeInsertQuery(columns, driver, tableName);
        updateQuery = writeUpdateQuery(columns, driver, tableName, idColumns);
    }

    public ColumnSchema getColumn(String name) {
        return columns.get(name);
    }

    public int getVersion(Object record) {
        int version = (int)versionCol.get(record);
        return version;
    }

    public void setVersion(Object record, int val) {
        versionCol.set(record, val);
    }

    public void persist(Connection con, Object record) {
        int version = getVersion(record);
        if(version == 0) {
            setVersion(record, 1);
            insert(con, record);
        } else {
            setVersion(record, version+1);
            update(con, record);
        }
    }

    public void update(Connection con, Object record) {
        try(PreparedStatement stmt = con.prepareStatement( updateQuery )) {
            throw new NotImplementedException();
        } catch (Exception ex) {
            throw new RuntimeException("Error updating record!", ex);
        }
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

    private static List<ColumnSchema> findIdCols(Map<String, ColumnSchema> columns) {
        List<ColumnSchema> idColumns = columns.values().stream()
                .filter(col -> col.getPkOrdinal() >= 0).collect(Collectors.toList());
        idColumns.sort((ColumnSchema a, ColumnSchema b) -> a.getPkOrdinal() - b.getPkOrdinal());
        return idColumns;
    }

    private static ColumnSchema findVersionCol(Map<String, ColumnSchema> columns) {
        for(ColumnSchema col : columns.values()) {
            if(col.isVersion()) {
                return col;
            }
        }
        return null;
    }

    private static String writeUpdateQuery(Map<String, ColumnSchema> columns,
                                           DbDriver driver, String tableName, List<ColumnSchema> idColumns) {
        Map<String, ColumnSchema> updateCols = new HashMap<>(columns);
        idColumns.forEach(col -> updateCols.remove(col));
        String updateClause = StringUtils.join(updateCols.keySet(), driver.ee() + "=?,\n" + driver.se());
        updateClause = driver.se() + updateClause + driver.ee() + "=?\n";
        String whereClause = StringUtils.join(idColumns, driver.ee() + " and " + driver.se());
        whereClause = driver.se() + whereClause + driver.ee();
        String sql = String.format( "update %s%s%s set %s where %s",
                driver.se(), tableName, driver.ee(), updateClause, whereClause );
        return sql;
    }

    private static String writeInsertQuery(Map<String, ColumnSchema> columns, DbDriver driver, String tableName) {
        String[] values = StringUtils.repeat("?", columns.size()).split("");
        String valueClause = StringUtils.join(values, ",");
        String delim = driver.ee() + "," + driver.se();
        String insertClause = driver.se() + StringUtils.join(columns.keySet(), delim) + driver.ee();
        String sql = String.format("insert into %s%s%s (%s) values (%s);",
                driver.se(), tableName, driver.ee(), insertClause, valueClause);
        return sql;
    }

    private static Map<String, ColumnSchema> parseAnnotations(Class<?> clazz) {
        // Collect accessors
        Map<String, Method> getters = new HashMap<>();
        Map<String, Method> setters = new HashMap<>();
        Map<String, Integer> pkFields = new HashMap<>();
        String versionCol = null;
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
            if(ano.pkOrdinal() > 0) {
                pkFields.put(colName, ano.pkOrdinal());
            }
            if(ano.isVersion()) {
                versionCol = colName;
            }
        }

        // Translate to columns
        Map<String, ColumnSchema> columns = new ConcurrentHashMap<>();
        for(Map.Entry<String, Method> entry : getters.entrySet()) {
            String colName = entry.getKey();
            Method getter = entry.getValue();
            Method setter = setters.get(colName);
            Integer pkOrdinal = pkFields.get(colName);
            boolean isVersion = StringUtils.equals(colName, versionCol);
            ColumnSchema col = new ColumnSchema(colName, getter, setter, pkOrdinal, isVersion);
            columns.put(colName, col);
        }
        return columns;
    }

}
