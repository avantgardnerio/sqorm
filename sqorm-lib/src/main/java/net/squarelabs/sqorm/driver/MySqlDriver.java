package net.squarelabs.sqorm.driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlDriver extends DbDriver {

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

}
