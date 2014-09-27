package net.squarelabs.sqorm.driver;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MsSqlDriver extends DbDriver {

    @Override
    public void resetDb(Connection con, String name) throws SQLException {
        try(Statement stmt = con.createStatement()) {
            stmt.execute(String.format("IF EXISTS(select * from sys.databases where name='%s') DROP DATABASE %s%s%s",
                    name, se(), name, ee()));
            stmt.execute(String.format("create database %s%s%s;", se(), name, ee()));
        }
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

}
