package net.squarelabs.sqorm.schema;

import net.squarelabs.sqorm.annotation.Column;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TableSchema {

    private final Class<?> clazz;

    public TableSchema(Class<?> clazz) {
        this.clazz = clazz;

        // Collect accessors
        Map<String, Method> getters = new HashMap<>();
        Map<String, Method> setters = new HashMap<>();
        for(Method method : clazz.getMethods()) {
            Column ano = method.getAnnotation(Column.class);
            if(ano == null) {
                continue;
            }
            String key = ano.name();
            Map<String, Method> map = method.getReturnType() == void.class ? setters : getters;
            map.put(key, method);
        }
    }
}
