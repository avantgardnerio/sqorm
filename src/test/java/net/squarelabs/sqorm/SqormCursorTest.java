package net.squarelabs.sqorm;

import com.googlecode.flyway.core.Flyway;
import net.squarelabs.sqorm.driver.DbDriver;
import net.squarelabs.sqorm.driver.DriverFactory;
import net.squarelabs.sqorm.schema.DbSchema;
import net.squarelabs.sqorm.schema.TableSchema;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SqormCursorTest {

    @Test
    public void canIterate() throws Exception {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername("sqorm");
        ds.setPassword("sqorm");
        ds.setUrl("jdbc:mysql://127.0.0.1/sqorm");

        boolean recordsValid = true;
        int count = 0;

        try(Connection con = ds.getConnection()) {
            // Clean DB
            DbDriver driver = DriverFactory.getDriver(con);
            driver.dropTables(con);

            // Rebuild DB
            Flyway flyway = new Flyway();
            flyway.setDataSource(ds);
            flyway.setLocations("ddl/mysql"); // TODO: Parameterize
            flyway.migrate();
            DbSchema db = new DbSchema(driver);

            // Add records
            TableSchema custTable = db.ensureTable(Customer.class);
            custTable.insert(con, new Customer(1, "alice"));
            custTable.insert(con, new Customer(2, "bob"));

            // Select from tables
            String selectQuery = "select 'net.squarelabs.sqorm.Customer' as classpath, customer.* from customer;";
            try(PreparedStatement stmt = con.prepareStatement(selectQuery)) {
                stmt.executeQuery();
                SqormCursor cur = new SqormCursor(db, stmt);
                while(cur.hasNext()) {
                    Object record = cur.next();
                    if(record == null) {
                        recordsValid = false;
                    }
                    count++;
                }
            }
        }
        Assert.assertEquals("All records iterable", 2, count);
        Assert.assertTrue("All records valid", recordsValid);
    }
}
