package net.squarelabs.sqorm;

import net.squarelabs.sqorm.schema.DbSchema;
import net.squarelabs.sqorm.schema.TableSchema;

import java.sql.Connection;

public class Persistor {

    private final DbSchema db;
    private final Connection con;

    public Persistor(DbSchema db, Connection con) {
        this.db = db;
        this.con = con;
    }

    public Object persist(Object record) {
        Class<?> clazz = record.getClass();
        TableSchema table = db.ensureTable(clazz);
        Object res = table.persist(con, record);
        return res;
    }

}
