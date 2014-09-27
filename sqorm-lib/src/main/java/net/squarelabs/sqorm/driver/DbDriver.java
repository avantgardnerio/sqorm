package net.squarelabs.sqorm.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class DbDriver {

    /**
     * Drops and recreates the given database
     *
     * @param con The connection to use
     * @param name The database to drop and re-create
     * @throws SQLException
     */
    public void resetDb(Connection con, String name) throws SQLException {
        try(Statement stmt = con.createStatement()) {
            stmt.execute(String.format("drop database if exists %s%s%s;", se(), name, ee()));
            stmt.execute(String.format("create database %s%s%s;", se(), name, ee()));
        }
    }

    public String rootDb() {
        return "information_schema";
    }

    public abstract String name();

    /**
     * @return Start escape char (e.g. "[", "`", "'")
     */
    public abstract char se();

    /**
     * @return End escape char (e.g. "]", "`", "'")
     */
    public abstract char ee();

    /**
     * @return true if DB supports Multiple Active Result Sets
     */
    public boolean mars() {
        return true;
    }
}
