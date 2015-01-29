package net.squarelabs.sqorm.codegen;

import com.google.common.base.CaseFormat;
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
        System.out.println("root=" + root);
        System.out.println("packageName=" + packageName);
        System.out.println("driver=" + driver);
        System.out.println("conString=" + conString);

        // Get package directory
        System.out.println("Creating folders...");
        String[] folders = packageName.split("\\.");
        File path = new File(root);
        for (String folder : folders) {
            path = new File(path.getAbsolutePath(), folder);
        }
        path.mkdirs();

        // Generate files
        System.out.println("Loading driver: " + driver);
        Class.forName(driver);
        System.out.println("Connecting to database...");
        try (Connection con = DriverManager.getConnection(conString)) {
            System.out.println("Loading schema...");
            Collection<Table> tables = loadSchema(con);
            System.out.println("Generating tables...");
            for (Table table : tables) {
                String fileName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table.getName()) + ".java";
                System.out.println("Generating " + fileName);
                String java = generateTableSource(table, packageName);
                File file = new File(path, fileName);
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

    public static String safeName(String name) {
        if("class".equals(name)) {
            return "clazz";
        }
        return name;
    }

    public static String generateTableSource(Table table, String packageName) {
        String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table.getName());
        StringBuilder sb = new StringBuilder();

        sb.append("package " + packageName + ";\n");
        sb.append("\n");
        sb.append("import " + packageName + ".*;\n");
        sb.append("import java.math.BigDecimal;\n");
        sb.append("import java.util.ArrayList;\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.UUID;\n");
        sb.append("import java.time.Instant;\n");
        sb.append("import java.util.Collection;\n");
        sb.append("import net.squarelabs.sqorm.annotation.Association;\n");
        sb.append("import org.codehaus.jackson.annotate.JsonIgnore;\n");
        sb.append("\n");
        sb.append("@net.squarelabs.sqorm.annotation.Table(name = \"" + table.getName() + "\")\n");
        sb.append("public class " + className + " {\n");
        sb.append("\n");

        // Private variables for column values
        for (Column col : table.getColumnChildren()) {
            String type = col.getDataType();
            sb.append("    private " + type + " " + safeName(col.getName()) + ";\n");
        }
        sb.append("\n");

        // Public accessors for column values
        for (Column col : table.getColumnChildren()) {
            String type = col.getDataType();

            sb.append("    @net.squarelabs.sqorm.annotation.Column(name=\"" + col.getName() + "\")\n");
            sb.append("    public " + type + " get" + col.getName() + "() {\n");
            sb.append("        return " + safeName(col.getName()) + ";\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    @net.squarelabs.sqorm.annotation.Column(name=\"" + col.getName() + "\")\n");
            sb.append("    public void set" + col.getName() + "(" + type + " val) {\n");
            sb.append("        this." + safeName(col.getName()) + " = val;\n");
            sb.append("    }\n");
            sb.append("\n");
        }

        // Child relationships
        sb.append("    // ------------------------------------------------------- Children -----------------------------------------------\n");
        for (Relation rel : table.getParentRelations()) {
            String annotation = buildAnnotation(rel, false);
            String foreignClassName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, rel.getForeignTableName());
            String varName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, rel.getForeignTableName());
            String childName = varName + "Children";
            String colName = "Collection<" + foreignClassName + ">";

            sb.append("\tprivate " + colName + " " + childName + ";\n");
            sb.append("\n");

            sb.append("\t" + annotation + "\n");
            sb.append("    public " + colName + " get" + foreignClassName + "Children() {\n");
            sb.append("        return " + childName + ";\n");
            sb.append("    }\n");
            sb.append("\n");

            sb.append("\t" + annotation + "\n");
            sb.append("    public void set" + foreignClassName + "Children(" + colName + " val) {\n");
            sb.append("        " + childName + " = val;\n");
            sb.append("    }\n");
        }
        sb.append("\n");

        // Parent relationships
        sb.append("    // ------------------------------------------------------- Parents ------------------------------------------------\n");
        for (Relation rel : table.getChildRelations()) {
            String annotation = buildAnnotation(rel, true);
            String primaryClassName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, rel.getPrimaryTableName());
            String varName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, rel.getPrimaryTableName());
            String parentName = varName + "Parent";

            sb.append("\tprivate " + primaryClassName + " " + parentName + ";\n");
            sb.append("\n");

            sb.append("\t" + annotation + "\n");
            sb.append("\t@JsonIgnore\n");
            sb.append("    public " + primaryClassName + " get" + primaryClassName + "Parent() {\n");
            sb.append("        return " + parentName + ";\n");
            sb.append("    }\n");
            sb.append("\n");

            sb.append("\t" + annotation + "\n");
            sb.append("\t@JsonIgnore\n");
            sb.append("    public void set" + primaryClassName + "Parent(" + primaryClassName + " val) {\n");
            sb.append("        " + parentName + " = val;\n");
            sb.append("    }\n");
        }
        sb.append("\n");

        // Cleanup
        sb.append("}");

        return sb.toString();
    }

    private static String buildAnnotation(Relation rel, boolean isForeignKey) {
        String pk = getPrimaryNames(rel);
        String fk = getForeignNames(rel);
        String annotation = String.format(
                "@Association(name = \"%s\", primaryKey = %s, foreignKey = %s, isForeignKey = %s)",
                rel.getName(), pk, fk, isForeignKey);
        return annotation;
    }

    private static String getPrimaryNames(Relation rel) {
        List<String> names = rel.getColumnChildren().stream()
                .sorted((a, b) -> a.getOrdinalPosition() - b.getOrdinalPosition())
                .map(RelationField::getPrimaryColumnName)
                .collect(Collectors.toList());
        String fields = Joiner.on("\",\"").join(names);
        return "{\"" + fields + "\"}";
    }

    private static String getForeignNames(Relation rel) {
        List<String> names = rel.getColumnChildren().stream()
                .sorted((a, b) -> a.getOrdinalPosition() - b.getOrdinalPosition())
                .map(RelationField::getForeignColumnName)
                .collect(Collectors.toList());
        String fields = Joiner.on("\",\"").join(names);
        return "{\"" + fields + "\"}";
    }

}
