package net.squarelabs.sqorm.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TypeConverter {

    public static Object sqlToJava(Class<?> clazz, ResultSet rs, int index) throws SQLException {
        // String
        if (clazz == String.class) {
            return rs.getString(index);
        }

        // Boolean
        if (clazz == boolean.class || clazz == Boolean.class) {
            return rs.getBoolean(index);
        }

        // Integer
        if (clazz == int.class || clazz == Integer.class) {
            return rs.getInt(index);
        }

        // UUID
        if (clazz == UUID.class) {
            Object obj = rs.getObject(index);
            if(obj instanceof String) {
                return UUID.fromString(rs.getString(index));
            }
            if(obj instanceof byte[]) {
                return UUID.nameUUIDFromBytes((byte[])obj);
            }
        }

        throw new RuntimeException("Can't convert ["
                + clazz.getCanonicalName() + "]");
    }
}
