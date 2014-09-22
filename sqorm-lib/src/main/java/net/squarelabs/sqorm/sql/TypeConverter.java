package net.squarelabs.sqorm.sql;

import java.math.BigInteger;

public class TypeConverter {

    public static Object SqlToJava(Class<?> clazz, Object value) {
        if(value == null) {
            return null;
        }

        if(clazz == String.class) {
            if(value instanceof String) {
                return value;
            }
        }
        if(clazz == boolean.class) {
            if(value instanceof Long) {
                return (long)value != 0;
            }
        }

        if(clazz == int.class || clazz == Integer.class) {
            if(value instanceof BigInteger) {
                return ((BigInteger)value).intValueExact();
            }
            if(value instanceof Integer) {
                return ((Integer)value).intValue();
            }
        }

        throw new RuntimeException("Can't convert ["
                + value.getClass().getCanonicalName()
                + "] to ["
                + clazz.getCanonicalName() + "]");
    }
}
