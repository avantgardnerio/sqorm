package net.squarelabs.sqorm;

import net.squarelabs.sqorm.codegen.Generator;
import net.squarelabs.sqorm.codegen.model.Column;
import net.squarelabs.sqorm.codegen.model.Table;
import net.squarelabs.sqorm.test.DbIntegrationTest;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.util.Collection;

public class GeneratorTest extends DbIntegrationTest {

    public GeneratorTest(String driverClass, String url, String dropUrl) {
        super(driverClass, url, dropUrl);
    }

    @Test
    public void generatorShouldGetSchema() throws Exception {
        try(Connection con = getPool().getConnection()) {
            Collection<Table> tables = Generator.loadSchema(con);
            Assert.assertEquals("Tables are present", tables.size(), 7);
            boolean hasColumns = true;
            for(Table table : tables) {
                Collection<Column> cols = table.getColumnChildren();
                if(cols.size() <= 0) {
                    hasColumns = false;
                }
                String java = Generator.generateTableSource(table, "net.squarelabs.sqorm.test");
                System.out.print(java);
            }
            Assert.assertTrue("Child column records loaded", hasColumns);
        }
    }
}
