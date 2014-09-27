package net.squarelabs.sqorm.driver;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostGresDriver extends DbDriver {

    @Override
    public String name() {
        return "postgres";
    }

    @Override
    public char se() {
        return '\"';
    }

    @Override
    public char ee() {
        return '\"';
    }

    @Override
    public boolean mars() {
        return false;
    }
}
