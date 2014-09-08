package net.squarelabs.sqorm.schema;

import com.google.common.base.Joiner;
import net.squarelabs.sqorm.annotation.Column;
import net.squarelabs.sqorm.annotation.Table;
import net.squarelabs.sqorm.driver.DbDriver;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableSchema {

    private final Class<?> clazz;
    private final String tableName;

    public TableSchema(Class<?> clazz, DbDriver driver) {
        this.clazz = clazz;
        tableName = clazz.getAnnotation(Table.class).name();

        // Collect accessors
        List<String> values = new ArrayList<>();
        Map<String, Method> insertFields = new HashMap<>();
        Map<String, Method> getters = new HashMap<>();
        Map<String, Method> setters = new HashMap<>();
        for(Method method : clazz.getMethods()) {
            Column ano = method.getAnnotation(Column.class);
            if(ano == null) {
                continue;
            }
            String colName = ano.name();
            Map<String, Method> map = method.getReturnType() == void.class ? setters : getters;
            map.put(colName, method);
            insertFields.put(colName, method);
            values.add("?");
        }

        // Write insert query
        String valueClause = StringUtils.join(values, ",");
        String delim = driver.ee() + "," + driver.se();
        String insertClause = driver.se() + StringUtils.join(insertFields.keySet(), delim) + driver.ee();
        String insertQuery = String.format("insert into %s%s$s (%s) values (%s);",
                driver.se(), tableName, driver.ee(), insertClause, valueClause);
    }

    public void insert(Connection con, Object record) {

    }
}
