package net.squarelabs.sqorm;

import net.squarelabs.sqorm.codegen.Generator;
import net.squarelabs.sqorm.codegen.model.Table;
import net.squarelabs.sqorm.dataset.Dataset;
import net.squarelabs.sqorm.driver.MySqlDriver;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

public class GeneratorTest {

    @Test
    public void generatorShouldGetSchema() throws Exception {
        // TODO: Move connection stuff into test base class
        BasicDataSource pool = new BasicDataSource();
        pool.setDriverClassName("com.mysql.jdbc.Driver");
        pool.setUsername("sqorm");
        pool.setPassword("sqorm");
        pool.setUrl("jdbc:mysql://127.0.0.1/sqorm?allowMultiQueries=true");

        try(Connection con = pool.getConnection()) {
            Collection<Table> tables = Generator.loadSchema(con);
            Assert.assertEquals("Tables are present", tables.size(), 4);
            boolean hasColumns = true;
            for(Table table : tables) {
                if(table.getColumnChildren().size() <= 0) {
                    hasColumns = false;
                }
            }
            Assert.assertTrue("Child column records loaded", hasColumns);
        }
    }
}
