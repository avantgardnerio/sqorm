package net.squarelabs.sqorm;

import com.googlecode.flyway.core.Flyway;
import net.squarelabs.sqorm.model.Customer;
import net.squarelabs.sqorm.model.Order;
import net.squarelabs.sqorm.dataset.Dataset;
import net.squarelabs.sqorm.driver.DbDriver;
import net.squarelabs.sqorm.driver.DriverFactory;
import net.squarelabs.sqorm.schema.DbSchema;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

public class DatasetTest {

    /**
     * This is sort of becoming the "god test"... need to split apart into real unit tests.
     * @throws Exception
     */
    @Test
    public void canIterate() throws Exception {
        BasicDataSource pool = new BasicDataSource();
        pool.setDriverClassName("com.mysql.jdbc.Driver");
        pool.setUsername("sqorm");
        pool.setPassword("sqorm");
        pool.setUrl("jdbc:mysql://127.0.0.1/sqorm?allowMultiQueries=true");

        try(Connection con = pool.getConnection()) {
            // Clean DB
            DbDriver driver = DriverFactory.getDriver(con);
            driver.dropTables(con);

            // Rebuild DB
            Flyway flyway = new Flyway();
            flyway.setDataSource(pool);
            flyway.setLocations("ddl/" + driver.name());
            flyway.migrate();
            DbSchema db = new DbSchema(driver);

            // Add records
            try(Persistor per = new Persistor(db, con)) {
                per.persist(new Customer(1, "alice"));
                per.persist(new Customer(2, "bob"));
                per.persist(new Order(1, 1));
                per.persist(new Order(2, 1));
            }

            // Select from tables
            QueryCache cache = new QueryCache(driver);
            Map<String,Object> parms = new HashMap<>();
            parms.put("CustomerId", 1);
            try(PreparedStatement stmt = cache.prepareQuery(con, "GetOrdersByCustomer", parms)) {
                stmt.executeQuery();
                Cursor cur = new Cursor(db, stmt);
                Dataset ds = new Dataset(db);
                ds.fill(cur);
                Assert.assertEquals("All parents iterable", 1, ds.ensureRecordset(Customer.class).size());
                Assert.assertEquals("All children iterable", 2, ds.ensureRecordset(Order.class).size());

                ds.attach(new Customer(3, "Charlie"));
                try(Persistor per = new Persistor(db, con)) {
                    ds.commit(per);
                }
            }
        }
    }

}
