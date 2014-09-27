package net.squarelabs.sqorm.driver;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class DriverFactory {

    public static DbDriver getDriver(Connection con) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        String dbName = metaData.getDatabaseProductName();
        switch(dbName) {
            case "PostgreSQL":
                return new PostGresDriver();
            case "MySQL":
                return new MySqlDriver();
            case "Microsoft SQL Server":
                return new MsSqlDriver();
            default:
                throw new RuntimeException("Unknown database type!");
        }
    }
}
