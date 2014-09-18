package net.squarelabs.sqorm.codegen;

import net.squarelabs.sqorm.annotation.Column;

@net.squarelabs.sqorm.annotation.Table(name = "Table")
public class Table {

    private String name;
    private String description;

    @Column(name="name")
    public String getName() {
        return name;
    }

    @Column(name="name")
    public void setName(String name) {
        this.name = name;
    }

    @Column(name="description")
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name="description")
    public String getDescription() {
        return description;
    }

}
