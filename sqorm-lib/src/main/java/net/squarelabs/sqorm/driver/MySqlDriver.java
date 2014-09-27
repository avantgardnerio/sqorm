package net.squarelabs.sqorm.driver;

import java.nio.ByteBuffer;
import java.util.UUID;

public class MySqlDriver extends DbDriver {

    @Override
    public String name() {
        return "mysql";
    }

    @Override
    public char se() {
        return '`';
    }

    @Override
    public char ee() {
        return '`';
    }

    @Override
    public Object javaToSql(Object value) {
        if(value instanceof UUID) {
            UUID uuid = (UUID)value;
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            return bb.array();
        }
        return super.javaToSql(value);
    }

}
