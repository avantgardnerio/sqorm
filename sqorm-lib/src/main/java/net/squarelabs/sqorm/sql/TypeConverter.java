package net.squarelabs.sqorm.sql;

import net.sourceforge.jtds.jdbc.ClobImpl;

import java.math.BigInteger;
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
        if(value instanceof UUID) {
            UUID uuid = (UUID)value;
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            return bb.array();
        }

        return value;
    }

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

    public static Object sqlToJava(Class<?> clazz, ResultSet rs, int index) throws SQLException {
        // String
        if(clazz == String.class) {
            return rs.getString(index);
        }

        // Boolean
        if(clazz == boolean.class || clazz == Boolean.class) {
            return rs.getBoolean(index);
        }

        // Integer
        if(clazz == int.class || clazz == Integer.class) {
            return rs.getInt(index);
        }

        // UUID
        if(clazz == UUID.class) {
            return UUID.fromString(rs.getString(index));
        }

        throw new RuntimeException("Can't convert ["
                + clazz.getCanonicalName() + "]");
    }
}
