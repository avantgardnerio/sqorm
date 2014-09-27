package net.squarelabs.sqorm.driver;

import net.squarelabs.sqorm.schema.ColumnSchema;
import org.apache.commons.lang.StringUtils;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class MySqlDriver extends DbDriver {

    @Override
    public String name() {
        return "mysql";
    }

    @Override
    public char se() {
        return '`';
    }

    @Override
    public char ee() {
        return '`';
    }

    @Override
    public Object javaToSql(Object value) {
        if (value instanceof UUID) {
            return value.toString().replace("-", "");
        }
        return super.javaToSql(value);
    }

    @Override
    public String writeUpdateQuery(SortedMap<String, ColumnSchema> updateCols,
                                   String tableName, List<ColumnSchema> idColumns) {
        String updateClause = buildClause(updateCols.values(), ",");
        String whereClause = buildClause(idColumns, " and ");
        String sql = String.format("update %s%s%s set\n%s where %s",
                se(), tableName, ee(), updateClause, whereClause);
        return sql;
    }

    private String buildClause(Collection<ColumnSchema> cols, String delim) {
        StringBuilder clause = new StringBuilder();
        for(ColumnSchema col : cols) {
            if(clause.length() > 0) {
                clause.append(delim);
            }
            clause.append("\t");
            clause.append(se());
            clause.append(col.getName());
            clause.append(ee());
            clause.append("=");
            if(col.getType() == UUID.class) {
                clause.append("unhex(?)");
            } else {
                clause.append("?");
            }
            clause.append("\n");
        }
        return clause.toString();
    }

    @Override
    public String writeInsertQuery(Map<String, ColumnSchema> insertCols, String tableName) {
        StringBuilder valueClause = new StringBuilder();
        for(ColumnSchema col : insertCols.values()) {
            if(valueClause.length() > 0) {
                valueClause.append(",");
            }
            if(col.getType() == UUID.class) {
                valueClause.append("unhex(?)");
            } else {
                valueClause.append("?");
            }
        }
        String delim = ee() + "," + se();
        List<String> keys = insertCols.values().stream().map(ColumnSchema::getName).collect(Collectors.toList());
        String insertClause = se() + StringUtils.join(keys, delim) + ee();
        String sql = String.format("insert into %s%s%s (%s) values (%s);",
                se(), tableName, ee(), insertClause, valueClause);
        return sql;
    }

}
