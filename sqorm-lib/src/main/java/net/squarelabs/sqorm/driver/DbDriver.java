package net.squarelabs.sqorm.driver;

import net.squarelabs.sqorm.schema.ColumnSchema;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class DbDriver {

    /**
     * Drops and recreates the given database
     *
     * @param con The connection to use
     * @param name The database to drop and re-create
     * @throws SQLException
     */
    public void resetDb(Connection con, String name) throws SQLException {
        try(Statement stmt = con.createStatement()) {
            stmt.execute(String.format("drop database if exists %s%s%s;", se(), name, ee()));
            stmt.execute(String.format("create database %s%s%s;", se(), name, ee()));
        }
    }

    public String writeUpdateQuery(SortedMap<String, ColumnSchema> updateCols,
                                           String tableName, List<ColumnSchema> idColumns) {
        List<String> idNames = idColumns.stream().map(col -> col.getName()).collect(Collectors.toList());
        List<String> keys = updateCols.values().stream().map(ColumnSchema::getName).collect(Collectors.toList());
        String updateClause = StringUtils.join(keys, ee() + "=?,\n" + se());
        updateClause = se() + updateClause + ee() + "=?\n";
        String whereClause = StringUtils.join(idNames, ee() + "=? and " + se());
        whereClause = se() + whereClause + ee() + "=?";
        String sql = String.format("update %s%s%s set %s where %s",
                se(), tableName, ee(), updateClause, whereClause);
        return sql;
    }

    public String writeInsertQuery(Map<String, ColumnSchema> columns, String tableName) {
        String[] values = StringUtils.repeat("?", columns.size()).split("");
        String valueClause = StringUtils.join(values, ",");
        String delim = ee() + "," + se();
        List<String> keys = columns.values().stream().map(ColumnSchema::getName).collect(Collectors.toList());
        String insertClause = se() + StringUtils.join(keys, delim) + ee();
        String sql = String.format("insert into %s%s%s (%s) values (%s);",
                se(), tableName, ee(), insertClause, valueClause);
        return sql;
    }

    public Object javaToSql(Object value) {
        return value;
    }

    public String rootDb() {
        return "information_schema";
    }

    public abstract String name();

    /**
     * @return Start escape char (e.g. "[", "`", "'")
     */
    public abstract char se();

    /**
     * @return End escape char (e.g. "]", "`", "'")
     */
    public abstract char ee();

    /**
     * @return true if DB supports Multiple Active Result Sets
     */
    public boolean mars() {
        return true;
    }

}
