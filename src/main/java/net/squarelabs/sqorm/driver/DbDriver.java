package net.squarelabs.sqorm.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DbDriver {
    public List<String> getTableNames(Connection con) throws SQLException;

    public void dropTables(Connection con) throws SQLException;
}
