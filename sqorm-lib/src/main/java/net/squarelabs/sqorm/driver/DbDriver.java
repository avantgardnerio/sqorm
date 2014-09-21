package net.squarelabs.sqorm.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DbDriver {
    public List<String> getTableNames(Connection con) throws SQLException;

    public void dropTables(Connection con) throws SQLException;

    public String name();

    /**
     * @return Start escape char (e.g. "[", "`", "'")
     */
    public char se();

    /**
     * @return End escape char (e.g. "]", "`", "'")
     */
    public char ee();

    /**
     * @return true if DB supports Multiple Active Result Sets
     */
    public boolean mars();
}
