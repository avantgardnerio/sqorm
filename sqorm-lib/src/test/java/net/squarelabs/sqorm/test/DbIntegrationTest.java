package net.squarelabs.sqorm.test;

import com.googlecode.flyway.core.Flyway;
import net.squarelabs.sqorm.driver.DbDriver;
import net.squarelabs.sqorm.driver.DriverFactory;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.*;

@RunWith(Parameterized.class)
public class DbIntegrationTest implements AutoCloseable {

    private final BasicDataSource pool;

    private static final List<DbIntegrationTest> instances = Collections.synchronizedList(new ArrayList<>());

    public DbIntegrationTest(String driverClass, String url) {
        pool = new BasicDataSource();
        pool.setDriverClassName(driverClass);
        pool.setUrl(url);
        instances.add(this);

        try(Connection con = pool.getConnection()) {
            DbDriver driver = DriverFactory.getDriver(con);
            driver.dropTables(con);

            // Rebuild DB
            Flyway flyway = new Flyway();
            flyway.setDataSource(getPool());
            flyway.setLocations("ddl/" + driver.name());
            flyway.migrate();
        } catch (Exception ex) {
            throw new RuntimeException("Error setting up database!", ex);
        }
    }

    public DataSource getPool() {
        return pool;
    }

    @Before
    public void migrate() {
    }

    @AfterClass
    public static void cleanup() {
        for (DbIntegrationTest inst : instances) {
            try {
                inst.close();
            } catch (Exception ex) {
                ex.printStackTrace(); // TODO: Logging
            }
        }
    }

    @Override
    public void close() throws Exception {
        pool.close();
    }

    @Parameterized.Parameters
    public static Collection getPools() {
        return Arrays.asList(new Object[][]{
                {"com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/sqorm?allowMultiQueries=true&user=sqorm&password=sqorm"},
                {"org.postgresql.Driver", "jdbc:postgresql://127.0.0.1/sqorm?user=sqorm&amp;password=sqorm"}
        });
    }

}
