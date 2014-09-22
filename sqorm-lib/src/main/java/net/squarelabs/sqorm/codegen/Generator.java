package net.squarelabs.sqorm.codegen;

import net.squarelabs.sqorm.sql.QueryCache;
import net.squarelabs.sqorm.codegen.model.Column;
import net.squarelabs.sqorm.codegen.model.Table;
import net.squarelabs.sqorm.dataset.Dataset;
import net.squarelabs.sqorm.driver.DbDriver;
import net.squarelabs.sqorm.driver.DriverFactory;
import net.squarelabs.sqorm.schema.DbSchema;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class Generator {

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public static Collection<Table> loadSchema(Connection con) throws SQLException {
        // TODO: Wrap this mess up in a single helper class
        DbDriver driver = DriverFactory.getDriver(con);
        DbSchema db = new DbSchema(driver, "net.squarelabs.sqorm.codegen.model");
        QueryCache cache = new QueryCache(driver, "interrogation");
        Dataset ds = new Dataset(db);
        ds.fill(cache, con, "interrogate", null);
        return ds.ensureRecordset(Table.class).all();
    }

    public static String generateTableSource(Table table, String packageName) {
        StringBuilder sb = new StringBuilder();

        sb.append("package " + packageName + ";\n");
        sb.append("\n");
        sb.append("import net.squarelabs.sqorm.codegen.Column;\n");
        sb.append("\n");
        sb.append("import java.util.ArrayList;\n");
        sb.append("import java.util.List;\n");
        sb.append("\n");
        sb.append("@net.squarelabs.sqorm.annotation.Table(name = \"" + table.getName() + "\")\n");
        sb.append("public class Table {\n");
        sb.append("\n");

        // Private variables for column values
        for(Column col : table.getColumnChildren()) {
            sb.append("    private String " + col.getName() + ";\n");
        }
        sb.append("\n");

        // Public accessors for column values
        for(Column col : table.getColumnChildren()) {
            sb.append("    @net.squarelabs.sqorm.annotation.Column(name=\"" + col.getName() + "\")\n");
            sb.append("    public String get" + col.getName() + " {\n");
            sb.append("        return " + col.getName() + ";\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    @net.squarelabs.sqorm.annotation.Column(name=\"" + col.getName() + "\")\n");
            sb.append("    public void set" + col.getName() + "(String val) {\n");
            sb.append("        this." + col.getName() + " = val;\n");
            sb.append("    }\n");
        }

        // Cleanup
        sb.append("}");

        return sb.toString();
    }
}
