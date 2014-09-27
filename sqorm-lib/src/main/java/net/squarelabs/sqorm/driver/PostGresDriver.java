package net.squarelabs.sqorm.driver;

public class PostGresDriver extends DbDriver {

    @Override
    public String rootDb() {
        return "postgres";
    }

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
