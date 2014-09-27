package net.squarelabs.sqorm.sql;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TypeConverter {

    /**
     * Called to translate Java types into SQL Parameter types, but only when the type is unknown
     * (like in a user-defined query).
     *
     * @param value The type to convert
     * @return The result
     */
    public static Object javaToSql(Object value) {
        if (value instanceof UUID) {
            return value.toString();
        }

        return value;
    }

    public static Object javaToSql(Class<?> clazz, Object value) {
        // PostGreSQL: http://crafted-software.blogspot.com/2013/03/uuid-values-from-jdbc-to-postgres.html
        if (clazz == UUID.class) {
            return value.toString();
        }

        return value;
    }

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
            return UUID.fromString(rs.getString(index));
        }

        throw new RuntimeException("Can't convert ["
                + clazz.getCanonicalName() + "]");
    }
}
