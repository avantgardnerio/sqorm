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
        String sql = "select * from test";

        try(Connection con = ds.getConnection()) {
            try(PreparedStatement stmt = con.prepareStatement(sql)) {
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
