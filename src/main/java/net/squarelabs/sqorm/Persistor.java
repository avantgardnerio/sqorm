package net.squarelabs.sqorm;

import net.squarelabs.sqorm.schema.DbSchema;
import net.squarelabs.sqorm.schema.TableSchema;

import java.sql.Connection;
import java.sql.SQLException;

public class Persistor implements AutoCloseable {

    private final DbSchema db;
    private final Connection con;
    private final boolean autoCommit;

    public Persistor(DbSchema db, Connection con) throws SQLException {
        this.db = db;
        this.con = con;
        this.autoCommit = con.getAutoCommit();
        con.setAutoCommit(false);
    }

    public void persist(Object record) {
        Class<?> clazz = record.getClass();
        TableSchema table = db.ensureTable(clazz);
        table.persist(con, record);
    }

    @Override
    public void close() throws Exception {
        try {
            con.commit();
        } finally {
            con.setAutoCommit(autoCommit);
        }
    }
}
