package net.squarelabs.sqorm;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            con.setAutoCommit(true);

            // use schema
            String useSchema = "use sqorm;";
            try(PreparedStatement stmt = con.prepareStatement(useSchema)) {
                stmt.execute();
            }

            // drop tables
            String dropCust = "drop table if exists customer;";
            try(PreparedStatement stmt = con.prepareStatement(dropCust)) {
                stmt.execute();
            }
            String dropOrder = "drop table if exists orders;";
            try(PreparedStatement stmt = con.prepareStatement(dropOrder)) {
                stmt.execute();
            }

            // Create customers
            String createTables = "CREATE TABLE customer (\n" +
                    "  `customer_id` INT NOT NULL,\n" +
                    "  `name` VARCHAR(45) NOT NULL,\n" +
                    "  PRIMARY KEY (`customer_id`));";
            try(PreparedStatement stmt = con.prepareStatement(createTables)) {
                stmt.execute();
            }

            // Create orders
            String createOrders = "CREATE TABLE orders (\n" +
                    "  `OrderId` INT NOT NULL,\n" +
                    "  `customer_id` INT NOT NULL,\n" +
                    "  PRIMARY KEY (`OrderId`));";
            try(PreparedStatement stmt = con.prepareStatement(createOrders)) {
                stmt.execute();
            }

            // Select from tables
            String selectQuery = "select 'net.squarelabs.sqorm.Customer' as classpath, customer.* from customer;";
            try(PreparedStatement stmt = con.prepareStatement(selectQuery)) {
                stmt.executeQuery();
                SqormCursor cur = new SqormCursor(stmt);
                while(cur.hasNext()) {
                    Object record = cur.next();
                    if(record == null) {
                        recordsValid = false;
                    }
                    count++;
                }
            }
        }
        Assert.assertEquals("All records iterable", 1, count);
        Assert.assertTrue("All records valid", recordsValid);
    }
}
