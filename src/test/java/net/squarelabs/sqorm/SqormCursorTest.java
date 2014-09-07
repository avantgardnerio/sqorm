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

            // drop tables
            String drop = "drop table if exists `sqorm`.`customer`;";
            try(PreparedStatement stmt = con.prepareStatement(drop)) {
                stmt.execute();
            }

            // Create customers
            String createTables = "CREATE TABLE `sqorm`.`customer` (\n" +
                    "  `customer_id` INT NOT NULL,\n" +
                    "  `name` VARCHAR(45) NOT NULL,\n" +
                    "  PRIMARY KEY (`customer_id`));";
            try(PreparedStatement stmt = con.prepareStatement(createTables)) {
                stmt.execute();
            }

            // Select from tables
            String selectQuery = "select * from customer;";
            try(PreparedStatement stmt = con.prepareStatement(selectQuery)) {
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
