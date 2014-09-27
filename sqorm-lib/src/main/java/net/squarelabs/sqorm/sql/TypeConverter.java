package net.squarelabs.sqorm.sql;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

public class TypeConverter {

    public static Object javaToSql(Class<?> clazz, Object value) {
        if(clazz == UUID.class) {
            UUID uuid = (UUID)value;
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            return bb.array();
        }

        return value;
    }

    public static Object sqlToJava(Class<?> clazz, Object value) {
        if(value == null) {
            return null;
        }

        // String
        if(clazz == String.class) {
            if(value instanceof String) {
                return value;
            }
        }

        // Boolean
        if(clazz == boolean.class || clazz == Boolean.class) {
            if(value instanceof Boolean) {
                return ((Boolean)value).booleanValue();
            }
            if(value instanceof Long) {
                return (long)value != 0;
            }
        }

        // Integer
        if(clazz == int.class || clazz == Integer.class) {
            if(value instanceof BigInteger) {
                return ((BigInteger)value).intValueExact();
            }
            if(value instanceof Long) {
                return ((Long)value).intValue();
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
