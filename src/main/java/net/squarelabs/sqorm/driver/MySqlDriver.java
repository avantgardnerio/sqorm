package net.squarelabs.sqorm.driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlDriver implements DbDriver {

    public MySqlDriver() {

    }

    @Override
    public List<String> getTableNames(Connection con) throws SQLException {
        List<String> list = new ArrayList<>();
        try(PreparedStatement stmt = con.prepareStatement("show tables;")) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                list.add(rs.getString(1));
            }
        }
        return list;
    }

    @Override
    public void dropTables(Connection con) throws SQLException {
        try(Statement stmt = con.createStatement()) {
            List<String> tableNames = getTableNames(con);
            for(String table : tableNames) {
                String sql = "drop table " + table + ";";
                stmt.execute(sql);
            }
        }
    }

    @Override
    public String se() {
        return "`";
    }

    @Override
    public String ee() {
        return "`";
    }

    @Override
    public boolean mars() {
        return true;
    }
}
