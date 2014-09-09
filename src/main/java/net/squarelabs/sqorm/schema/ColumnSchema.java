package net.squarelabs.sqorm.schema;

import java.lang.reflect.Method;

public class ColumnSchema {

    private final String name;
    private final Method getter;
    private final Method setter;

    public ColumnSchema(String name, Method getter, Method setter) {
        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    public String getName() {
        return name;
    }

    public Object get(Object record) {
        try {
            return getter.invoke(record);
        } catch (Exception ex) {
            throw new RuntimeException("Error setting record value!", ex);
        }
    }
    public void set(Object record, Object val) {
        try {
            setter.invoke(record, new Object[] {val});
        } catch (Exception ex) {
            throw new RuntimeException("Error setting record value!", ex);
        }
    }
}
