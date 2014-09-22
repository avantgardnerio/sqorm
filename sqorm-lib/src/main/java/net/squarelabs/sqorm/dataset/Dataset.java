package net.squarelabs.sqorm.dataset;

import net.squarelabs.sqorm.Cursor;
import net.squarelabs.sqorm.Persistor;
import net.squarelabs.sqorm.fluent.Query;
import net.squarelabs.sqorm.index.BaseIndex;
import net.squarelabs.sqorm.index.Key;
import net.squarelabs.sqorm.schema.RelationSchema;
import net.squarelabs.sqorm.schema.TableSchema;
import net.squarelabs.sqorm.sql.QueryCache;
import net.squarelabs.sqorm.schema.DbSchema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

    public Recordset getRecordset(Class<?> clazz) {
        return recordsets.get(clazz);
    }

    public Query from(Class<?> clazz) {
        return ensureRecordset(clazz);
    }

    public Recordset ensureRecordset(Class<?> clazz) {
        Recordset rs = recordsets.get(clazz);
        if(rs != null) {
            return rs;
        }
        synchronized (this) {
            TableSchema table = db.getTable(clazz);
            rs = new Recordset(table);
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
        // Populate rows
        Set<Recordset> rses = new HashSet<>();
        for(Object record : cursor) {
            Class<?> clazz = record.getClass();
            Recordset rs = ensureRecordset(clazz);
            rs.add(record);
        }
        hookupReferences();
    }

    private void hookupReferences() {
        for(RelationSchema rel : db.getRelationships().values()) {
            Recordset parentRs = getRecordset(rel.getPrimaryTable().getType());
            if(parentRs == null) {
                continue; // Not loaded yet
            }
            Recordset childRs = getRecordset(rel.getForeignTable().getType());
            if(childRs == null) {
                continue; // Not loaded yet
            }
            BaseIndex childIdx = childRs.getIndex(rel.getForeignIndex());

            for(Object parentRecord : parentRs) {
                Key key = rel.getPrimaryIndex().getKey(parentRecord);
                Set<Object> children = childIdx.find(key);
                rel.setChildren(parentRecord, children);
                for(Object childRecord : children) {
                    rel.setParent(childRecord, parentRecord);
                }
            }
        }
    }

}
