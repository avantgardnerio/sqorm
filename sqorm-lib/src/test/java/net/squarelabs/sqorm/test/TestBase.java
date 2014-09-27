package net.squarelabs.sqorm.test;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.sql.DataSource;
import java.util.*;

@RunWith(Parameterized.class)
public class TestBase implements AutoCloseable {

    private final BasicDataSource pool;

    private static final List<TestBase> instances = Collections.synchronizedList(new ArrayList<>());

    public TestBase(String driverClass, String url) {
        pool = new BasicDataSource();
        pool.setDriverClassName(driverClass);
        pool.setUrl(url);
        instances.add(this);
    }

    public DataSource getPool() {
        return pool;
    }

    @AfterClass
    public static void cleanup() {
        for (TestBase inst : instances) {
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
