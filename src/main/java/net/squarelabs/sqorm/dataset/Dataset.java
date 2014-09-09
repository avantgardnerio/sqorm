package net.squarelabs.sqorm.dataset;

import net.squarelabs.sqorm.Cursor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Dataset {

    private final Map<Class<?>, Recordset> recordsets = new ConcurrentHashMap<>();

    public Recordset ensureRecordset(Class<?> clazz) {
        Recordset rs = recordsets.get(clazz);
        if(rs != null) {
            return rs;
        }
        synchronized (this) {
            rs = new Recordset(clazz);
            recordsets.put(clazz, rs);
            return rs;
        }
    }

    public void fill(Cursor cursor) {
        for(Object record : cursor) {
            Class<?> clazz = record.getClass();
            Recordset rs = ensureRecordset(clazz);
            rs.add(record);
        }
    }
}
