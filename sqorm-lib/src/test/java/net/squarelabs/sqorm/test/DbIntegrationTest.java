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
import java.sql.DriverManager;
import java.util.*;

@RunWith(Parameterized.class)
public class DbIntegrationTest implements AutoCloseable {

    private final BasicDataSource pool;

    private static final String username = "sqorm";
    private static final String password = "sqorm";

    private static final List<DbIntegrationTest> instances = Collections.synchronizedList(new ArrayList<>());

    public DbIntegrationTest(String driverClass, String url, String dropUrl) {
        if(dropUrl == null) {
            dropUrl = url; // PostGreSQL doesn't support switching databases, so we need to do this work-around
        }

        try(Connection dropCon = DriverManager.getConnection(dropUrl, username, password)) {
            // Drop DB
            DbDriver driver = DriverFactory.getDriver(dropCon);
            driver.resetDb(dropCon, "sqorm");

            // Create connection pool
            pool = new BasicDataSource();
            pool.setDriverClassName(driverClass);
            pool.setUrl(url);
            pool.setDefaultCatalog("sqorm");
            pool.setUsername(username);
            pool.setPassword(password);
            instances.add(this);

            // Rebuild DB
            Flyway flyway = new Flyway();
            flyway.setDataSource(getPool());
            flyway.setLocations("ddl/" + driver.name());
            flyway.migrate();
        } catch (Exception ex) {
            throw new RuntimeException("Error dropping database!", ex);
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
        // TODO: Throw this in a resource file
        return Arrays.asList(new Object[][]{
                //{"net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://127.0.0.1", null}
                {"com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1?allowMultiQueries=true", null}
                //{"org.postgresql.Driver", "jdbc:postgresql://127.0.0.1/sqorm", "jdbc:postgresql://127.0.0.1/postgres"}
        });
    }

}
