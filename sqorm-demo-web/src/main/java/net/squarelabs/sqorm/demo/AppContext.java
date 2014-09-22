package net.squarelabs.sqorm.demo;

import net.squarelabs.sqorm.driver.DbDriver;
import net.squarelabs.sqorm.driver.DriverFactory;
import net.squarelabs.sqorm.schema.DbSchema;
import net.squarelabs.sqorm.sql.QueryCache;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;

public class AppContext {

    private static BasicDataSource pool;
    private static DbDriver driver;
    private static DbSchema db;
    private static QueryCache cache;

    public static void initialize() {
        try {
            if(pool != null) {
                throw new RuntimeException("Application already initialized!");
            }

            // Pool
            String driverName = System.getProperty("sql.driver");
            Class.forName(driverName);
            String conString = System.getProperty("sql.connection.string");
            pool = new BasicDataSource();
            pool.setDriverClassName(driverName);
            pool.setUrl(conString);

            // Sqorm
            try(Connection con = pool.getConnection()) {
                driver = DriverFactory.getDriver(con);
                db = new DbSchema(driver, "net.squarelabs.sqorm.demo");
                cache = new QueryCache(driver);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error stating app!", ex);
        }
    }

    public static void destroy() {
        driver = null;
        db = null;
        cache = null;

        try {
            pool.close();
        } catch (Exception ex) {
            throw new RuntimeException("Error closing pool!", ex);
        } finally {
            pool = null;
        }
    }

    public static QueryCache getQueryCache() {
        return cache;
    }

    public static DbSchema getSchema() {
        return db;
    }

    public static DbDriver getDriver() {
        return driver;
    }

    public static DataSource getPool() {
        return pool;
    }

}
