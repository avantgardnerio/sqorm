package net.squarelabs.sqorm.sql;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
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
            if(obj instanceof UUID) {
                return obj;
            }
            if(obj instanceof String) {
                String str = (String)obj;
                if(!str.contains("-")) {
                    str = str.substring(0, 8) + "-"
                            + str.substring(8, 12) + "-"
                            + str.substring(12, 16) + "-"
                            + str.substring(16, 20) + "-"
                            + str.substring(20, 32);
                }
                UUID uuid = UUID.fromString(str);
                return uuid;
            }
            if(obj instanceof byte[]) {
                return UUID.nameUUIDFromBytes((byte[])obj);
            }
        }

        throw new RuntimeException("Can't convert ["
                + clazz.getCanonicalName() + "]");
    }
}
