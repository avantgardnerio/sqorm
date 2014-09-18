package net.squarelabs.sqorm.codegen;

import net.squarelabs.sqorm.codegen.Column;

import java.util.ArrayList;
import java.util.List;

@net.squarelabs.sqorm.annotation.Table(name = "Table")
public class Table {

    private String name;
    private String description;

    private final List<Column> columnChildren = new ArrayList<>();

    @net.squarelabs.sqorm.annotation.Column(name="name", pkOrdinal = 0)
    public String getName() {
        return name;
    }

    @net.squarelabs.sqorm.annotation.Column(name="name", pkOrdinal = 0)
    public void setName(String name) {
        this.name = name;
    }

    @net.squarelabs.sqorm.annotation.Column(name="description")
    public void setDescription(String description) {
        this.description = description;
    }

    @net.squarelabs.sqorm.annotation.Column(name="description")

    // TODO: Relationship annotations
    public String getDescription() {
        return description;
    }

    // TODO: Relationship annotations
    public List<Column> getColumnChildren() {
        return columnChildren;
    }

}
