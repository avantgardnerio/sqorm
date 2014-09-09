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
    public String name() {
        return "mysql";
    }

    @Override
    public char se() {
        return '`';
    }

    @Override
    public char ee() {
        return '`';
    }

    @Override
    public boolean mars() {
        return true;
    }
}
