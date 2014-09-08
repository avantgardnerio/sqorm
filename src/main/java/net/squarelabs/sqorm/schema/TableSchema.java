package net.squarelabs.sqorm.schema;

import com.google.common.base.Joiner;
import net.squarelabs.sqorm.annotation.Column;
import net.squarelabs.sqorm.annotation.Table;
import net.squarelabs.sqorm.driver.DbDriver;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableSchema {

    private final Class<?> clazz;
    private final String tableName;

    // Reflection cache
    private final Map<String, Method> insertFields = new HashMap<>();


    // Cached query syntax
    private final String insertQuery;

    public TableSchema(Class<?> clazz, DbDriver driver) {
        this.clazz = clazz;
        tableName = clazz.getAnnotation(Table.class).name();

        // Collect accessors
        List<String> values = new ArrayList<>();
        Map<String, Method> getters = new HashMap<>();
        Map<String, Method> setters = new HashMap<>();
        for(Method method : clazz.getMethods()) {
            Column ano = method.getAnnotation(Column.class);
            if(ano == null) {
                continue;
            }
            String colName = ano.name();
            if(method.getReturnType() == void.class) {
                setters.put(colName, method);
            } else {
                getters.put(colName, method);
                insertFields.put(colName, method);
            }
            values.add("?");
        }

        // Write insert query
        String valueClause = StringUtils.join(values, ",");
        String delim = driver.ee() + "," + driver.se();
        String insertClause = driver.se() + StringUtils.join(insertFields.keySet(), delim) + driver.ee();
        insertQuery = String.format("insert into %s%s$s (%s) values (%s);",
                driver.se(), tableName, driver.ee(), insertClause, valueClause);
    }

    public void insert(Connection con, Object record) {
        try(PreparedStatement stmt = con.prepareStatement(insertQuery)) {
            int i = 1;
            // TODO: Use TreeMap, List<Tuple>, or otherwise ensure order somehow!
            for(Map.Entry<String, Method> entry : insertFields.entrySet()) {
                Method getter = entry.getValue();
                Object val = getter.invoke(record);
                stmt.setObject(i++, val);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error inserting record!", ex);
        }
    }
}
