package net.squarelabs.sqorm;

public class Query {
    private final String sql;
    private final String[] parameters;

    public Query(String sql, String[] parameters) {
        this.sql = sql;
        this.parameters = parameters;
    }

    public String getSql() {
        return sql;
    }

    public String[] getParameters() {
        return parameters;
    }

}
