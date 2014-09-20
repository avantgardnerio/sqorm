package net.squarelabs.sqorm.sql;

public class SqlQuery {
    private final String sql;
    private final String[] parameters;

    public SqlQuery(String sql, String[] parameters) {
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
