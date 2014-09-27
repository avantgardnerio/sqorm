package net.squarelabs.sqorm.codegen;

import com.google.common.base.Joiner;
import net.squarelabs.sqorm.codegen.model.Relation;
import net.squarelabs.sqorm.codegen.model.RelationField;
import net.squarelabs.sqorm.sql.QueryCache;
import net.squarelabs.sqorm.codegen.model.Column;
import net.squarelabs.sqorm.codegen.model.Table;
import net.squarelabs.sqorm.dataset.Dataset;
import net.squarelabs.sqorm.driver.DbDriver;
import net.squarelabs.sqorm.driver.DriverFactory;
import net.squarelabs.sqorm.schema.DbSchema;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Generator {

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            throw new RuntimeException("Invalid number of arguments!");
        }
        String root = args[0];
        String packageName = args[1];
        String driver = args[2];
        String conString = args[3];

        // Get package directory
        String[] folders = packageName.split("\\.");
        File path = new File(root);
        for (String folder : folders) {
            path = new File(path.getAbsolutePath(), folder);
        }
        path.mkdirs();

        // Generate files
        Class.forName(driver);
        System.out.println("Connecting to database...");
        try (Connection con = DriverManager.getConnection(conString)) {
            System.out.println("Loading schema...");
            Collection<Table> tables = loadSchema(con);
            for (Table table : tables) {
                System.out.println("Generating " + table.getName() + ".java");
                String java = generateTableSource(table, packageName);
                File file = new File(path, table.getName() + ".java");
                try (FileWriter fw = new FileWriter(file)) {
                    try (BufferedWriter writer = new BufferedWriter(fw)) {
                        writer.write(java);
                    }
                }
            }
        }
        System.out.println("Code generation complete!");
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
        sb.append("import java.util.ArrayList;\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.UUID;\n");
        sb.append("import java.time.Instant;\n");
        sb.append("import java.util.Collection;\n");
        sb.append("import net.squarelabs.sqorm.annotation.Association;\n");
        sb.append("\n");
        sb.append("@net.squarelabs.sqorm.annotation.Table(name = \"" + table.getName() + "\")\n");
        sb.append("public class " + table.getName() + " {\n");
        sb.append("\n");

        // Private variables for column values
        for (Column col : table.getColumnChildren()) {
            String type = col.getDataType();
            sb.append("    private " + type + " " + col.getName() + ";\n");
        }
        sb.append("\n");

        // Public accessors for column values
        for (Column col : table.getColumnChildren()) {
            String type = col.getDataType();

            sb.append("    @net.squarelabs.sqorm.annotation.Column(name=\"" + col.getName() + "\")\n");
            sb.append("    public " + type + " get" + col.getName() + "() {\n");
            sb.append("        return " + col.getName() + ";\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    @net.squarelabs.sqorm.annotation.Column(name=\"" + col.getName() + "\")\n");
            sb.append("    public void set" + col.getName() + "(" + type + " val) {\n");
            sb.append("        this." + col.getName() + " = val;\n");
            sb.append("    }\n");
            sb.append("\n");
        }

        // Parent relationships
        for (Relation rel : table.getParentRelations()) {

        }

        // Child relationships
        for (Relation rel : table.getChildRelations()) {
            List<String> primaryFieldNames = rel.getColumnChildren().stream()
                    .sorted((a, b) -> a.getOrdinalPosition() - b.getOrdinalPosition())
                    .map(RelationField::getPrimaryColumnName)
                    .collect(Collectors.toList());
            List<String> foreignFieldNames = rel.getColumnChildren().stream()
                    .sorted((a, b) -> a.getOrdinalPosition() - b.getOrdinalPosition())
                    .map(RelationField::getForeignColumnName)
                    .collect(Collectors.toList());
            String annotation = String.format(
                    "@Association(name = \"%s\", primaryKey = \"%s\", foreignKey = \"%s\", isForeignKey = false)",
                    rel.getName(), Joiner.on(",").join(primaryFieldNames), Joiner.on(",").join(foreignFieldNames));

            sb.append("\t" + annotation + "\n");
            sb.append("    public Collection<" + rel.getForeignTableName() + "> get" + rel.getForeignTableName() + "Children() {\n");
            sb.append("        return " + rel.getForeignTableName() + "Children;\n");
            sb.append("    }\n");
            sb.append("\n");

            sb.append("\t" + annotation + "\n");
            sb.append("    public void set" + rel.getForeignTableName() + "(Collection<" + rel.getForeignTableName() + "> val) {\n");
            sb.append("        " + rel.getForeignTableName() + "Children = val;\n");
            sb.append("    }\n");
        }

        // Cleanup
        sb.append("}");

        return sb.toString();
    }

}
