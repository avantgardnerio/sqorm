package net.squarelabs.sqorm;

import net.squarelabs.sqorm.driver.MySqlDriver;
import net.squarelabs.sqorm.sql.QueryCache;
import net.squarelabs.sqorm.sql.SqlQuery;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class QueryCacheTest {

    @Test
    public void loadStatementShouldReplaceNamedParms() {
        String sql = "select * from customer where customer_id=@CustomerId and name=@CustomerName";
        String expectedSql = "select * from customer where customer_id=? and name=?;";
        String[] expectedParms = new String[] { "CustomerId", "CustomerName" };
        SqlQuery actual = QueryCache.loadStatement(sql, new MySqlDriver());
        Assert.assertEquals(expectedSql, actual.getSql());
        stringArrayEquals(expectedParms, actual.getParameters());
    }

    @Test
    public void querySplitterShouldSplit() {
        String sql = "select * from customer; select * from order;";
        String[] expected = new String[] {
                "select * from customer;",
                "select * from order;"
        };
        String[] actual = QueryCache.splitQuery(sql, false).toArray(new String[] {});
        stringArrayEquals(expected, actual);
    }

    @Test
    public void querySplitterShouldHandleQuotes() {
        String sql = "select 'te;st', c.* from customer c; select * from order;";
        String[] expected = new String[] {
                "select 'te;st', c.* from customer c;",
                "select * from order;"
        };
        String[] actual = QueryCache.splitQuery(sql, false).toArray(new String[] {});
        stringArrayEquals(expected, actual);
    }

    @Test
    public void querySplitterShouldHandleNestedQuotes() {
        String sql = "select 'te\\';st', c.* from customer c; select * from order;";
        String[] expected = new String[] {
                "select 'te\\';st', c.* from customer c;",
                "select * from order;"
        };
        String[] actual = QueryCache.splitQuery(sql, false).toArray(new String[] {});
        stringArrayEquals(expected, actual);
    }

    private static void stringArrayEquals(String[] expected, String[] actual) {
        Assert.assertEquals(expected.length, actual.length);
        boolean areEqual = true;
        for(int i = 0; i < expected.length; i++) {
            if(!StringUtils.equals(expected[i], actual[i])) {
                areEqual = false;
            }
        }
        Assert.assertTrue(areEqual);
    }
}
