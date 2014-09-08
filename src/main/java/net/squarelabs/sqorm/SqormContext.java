package net.squarelabs.sqorm;

import javax.sql.DataSource;

public class SqormContext {

    private DataSource pool;

    public SqormContext(DataSource pool) {
        if(pool == null) {
            throw new IllegalArgumentException("DataSource cannot be null!");
        }

        this.pool = pool;
    }

}
