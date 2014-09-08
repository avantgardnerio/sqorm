package net.squarelabs.sqorm.driver;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class DriverFactory {
    public static DbDriver createWiper(Connection con) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        String dbName = metaData.getDatabaseProductName();
        switch(dbName) {
            case "PostgreSQL":
                throw new RuntimeException("PostGreSQL not yet supported!");
            case "MySQL":
                return new MySqlDriver(con);
            case "Microsoft SQL Server":
                throw new RuntimeException("MS-SQL not yet supported!");
            default:
                throw new RuntimeException("Unknown database type!");
        }
    }
}
