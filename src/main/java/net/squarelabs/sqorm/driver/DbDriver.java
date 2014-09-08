package net.squarelabs.sqorm.driver;

import java.util.List;

public interface DbDriver {
    public List<String> getTableNames();

    public void dropTables();
}
