package net.squarelabs.sqorm;

import net.squarelabs.sqorm.dataset.Dataset;
import net.squarelabs.sqorm.driver.DbDriver;
import net.squarelabs.sqorm.driver.DriverFactory;
import net.squarelabs.sqorm.model.Customer;
import net.squarelabs.sqorm.model.Order;
import net.squarelabs.sqorm.schema.DbSchema;
import net.squarelabs.sqorm.sql.QueryCache;
import net.squarelabs.sqorm.test.DbIntegrationTest;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class DatasetTest extends DbIntegrationTest {

    public DatasetTest(String driverClass, String url) {
        super(driverClass, url);
    }

    /**
     * This is sort of becoming the "god test"... need to split apart into real unit tests.
     * @throws Exception
     */
    @Test
    public void canIterate() throws Exception {
        try(Connection con = getPool().getConnection()) {
            DbDriver driver = DriverFactory.getDriver(con);
            DbSchema db = new DbSchema(driver, "net.squarelabs.sqorm.model");

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
            Dataset ds = new Dataset(db);
            ds.fill(cache, con, "GetOrdersByCustomer", parms);
            Assert.assertEquals("All parents iterable", 1, ds.ensureRecordset(Customer.class).size());
            Assert.assertEquals("All children iterable", 2, ds.ensureRecordset(Order.class).size());

            ds.attach(new Customer(3, "Charlie"));
            try(Persistor per = new Persistor(db, con)) {
                ds.commit(per);
            }
        }
    }

}
