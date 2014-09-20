package net.squarelabs.sqorm.dataset;

import net.squarelabs.sqorm.Cursor;
import net.squarelabs.sqorm.Persistor;
import net.squarelabs.sqorm.sql.QueryCache;
import net.squarelabs.sqorm.schema.DbSchema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Dataset {

    private final DbSchema db;
    private final Map<Class<?>, Recordset> recordsets = new ConcurrentHashMap<>();

    public Dataset(DbSchema db) {
        this.db = db;
    }

    public void commit(Persistor persistor) throws SQLException {
        for(Recordset rs : recordsets.values()) {
            for(Object record : rs) {
                persistor.persist(record);
            }
        }
    }

    public void attach(Object record) {
        Class<?> clazz = record.getClass();
        Recordset rs = ensureRecordset(clazz);
        rs.add(record);
    }

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

    public void fill(QueryCache cache, Connection con, String queryName, Map<String,Object> parms) {
        try(PreparedStatement stmt = cache.prepareQuery(con, queryName, parms)) {
            stmt.executeQuery();
            fill(stmt);
        } catch (Exception ex) {
            throw new RuntimeException("Error running query " + queryName, ex);
        }
    }

    public void fill(PreparedStatement stmt) throws SQLException {
        Cursor cur = new Cursor(db, stmt);
        fill(cur);
    }

    public void fill(Cursor cursor) {
        for(Object record : cursor) {
            Class<?> clazz = record.getClass();
            Recordset rs = ensureRecordset(clazz);
            rs.add(record);
        }
    }
}
