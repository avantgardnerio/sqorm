package net.squarelabs.sqorm;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SqormContext {

    private DataSource pool;

    public SqormContext(DataSource pool) {
        if(pool == null) {
            throw new IllegalArgumentException("DataSource cannot be null!");
        }

        this.pool = pool;
    }

}
