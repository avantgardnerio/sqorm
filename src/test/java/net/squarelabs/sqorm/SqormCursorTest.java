package net.squarelabs.sqorm;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqormCursorTest {
    @Test
    public void canIterate() throws Exception {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername("root");
        ds.setPassword("root");
        ds.setUrl("jdbc:mysql://localhost/test");

        boolean recordsValid = true;
        int count = 0;
        try(Connection con = ds.getConnection()) {
            try(Statement stmt = con.createStatement()) {
                String sql = "select * from test";
                try(ResultSet res = stmt.executeQuery(sql)) {
                    SqormCursor cur = new SqormCursor(res);
                    while(cur.hasNext()) {
                        Object record = cur.next();
                        if(record == null) {
                            recordsValid = false;
                        }
                        count++;
                    }
                }
            }
        }
        Assert.assertEquals("All records iterable", 1, count);
        Assert.assertTrue("All records valid", recordsValid);
    }
}
