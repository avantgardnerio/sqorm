package net.squarelabs.sqorm.codegen;

import net.squarelabs.sqorm.codegen.model.Column;
import net.squarelabs.sqorm.codegen.model.Table;

public class Generator {
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
