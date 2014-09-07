package net.squarelabs.sqorm;

import org.apache.commons.dbcp.*;
import org.junit.Assert;
import org.junit.Test;

public class SqormContextTest {
    @Test
    public void canInstantiate() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername("root");
        ds.setPassword("root");
        ds.setUrl("jdbc:mysql://localhost/test");

        SqormContext ctx = new SqormContext(ds);
        Assert.assertNotNull(ctx);
    }
}
