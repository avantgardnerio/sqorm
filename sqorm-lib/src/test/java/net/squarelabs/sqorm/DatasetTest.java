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
import java.util.UUID;

public class DatasetTest extends DbIntegrationTest {

    public DatasetTest(String driverClass, String url, String dropUrl) {
        super(driverClass, url, dropUrl);
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
            UUID aid = UUID.randomUUID();
            UUID bid = UUID.randomUUID();
            try(Persistor per = new Persistor(db, con)) {
                per.persist(new Customer(aid, "alice"));
                per.persist(new Customer(bid, "bob"));
                per.persist(new Order(aid));
                per.persist(new Order(aid));
            }

            // Select from tables
            QueryCache cache = new QueryCache(driver);
            Map<String,Object> parms = new HashMap<>();
            parms.put("CustomerId", aid);
            Dataset ds = new Dataset(db);
            ds.fill(cache, con, "GetOrdersByCustomer", parms);
            Assert.assertEquals("All parents iterable", 1, ds.ensureRecordset(Customer.class).size());
            Assert.assertEquals("All children iterable", 2, ds.ensureRecordset(Order.class).size());

            ds.attach(new Customer(UUID.randomUUID(), "Charlie"));
            try(Persistor per = new Persistor(db, con)) {
                ds.commit(per);
            }
        }
    }

}
