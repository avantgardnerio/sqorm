package net.squarelabs.sqorm.driver;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MsSqlDriver implements DbDriver {

    public MsSqlDriver() {

    }

    @Override
    public List<String> getTableNames(Connection con) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public void dropTables(Connection con) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public String name() {
        return "mssql";
    }

    @Override
    public char se() {
        return '[';
    }

    @Override
    public char ee() {
        return ']';
    }

    @Override
    public boolean mars() {
        return true;
    }
}
