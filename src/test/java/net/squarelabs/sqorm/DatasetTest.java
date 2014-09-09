package net.squarelabs.sqorm;

import com.googlecode.flyway.core.Flyway;
import net.squarelabs.sqorm.dataset.Dataset;
import net.squarelabs.sqorm.driver.DbDriver;
import net.squarelabs.sqorm.driver.DriverFactory;
import net.squarelabs.sqorm.schema.DbSchema;
import net.squarelabs.sqorm.schema.TableSchema;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DatasetTest {
    @Test
    public void canIterate() throws Exception {
        BasicDataSource pool = new BasicDataSource();
        pool.setDriverClassName("com.mysql.jdbc.Driver");
        pool.setUsername("sqorm");
        pool.setPassword("sqorm");
        pool.setUrl("jdbc:mysql://127.0.0.1/sqorm");

        try(Connection con = pool.getConnection()) {
            // Clean DB
            DbDriver driver = DriverFactory.getDriver(con);
            driver.dropTables(con);

            // Rebuild DB
            Flyway flyway = new Flyway();
            flyway.setDataSource(pool);
            flyway.setLocations("ddl/mysql"); // TODO: Parameterize
            flyway.migrate();
            DbSchema db = new DbSchema(driver);

            // Add records
            TableSchema custTable = db.ensureTable(Customer.class);
            custTable.persist(con, new Customer(1, "alice"));
            custTable.persist(con, new Customer(2, "bob"));

            // Select from tables
            String selectQuery = "select 'net.squarelabs.sqorm.Customer' as classpath, customer.* from customer;";
            try(PreparedStatement stmt = con.prepareStatement(selectQuery)) {
                stmt.executeQuery();
                Cursor cur = new Cursor(db, stmt);
                Dataset ds = new Dataset(db);
                ds.fill(cur);
                Assert.assertEquals("All records iterable", 2, ds.ensureRecordset(Customer.class).size());

                ds.attach(new Customer(3, "Charlie"));
                try(Persistor per = new Persistor(db, con)) {
                    ds.commit(per);
                }
            }
        }
    }

}
