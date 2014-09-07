package net.squarelabs.sqorm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class SqormCursor<T> implements Iterator<T> {
    private ResultSet resultSet;
    private ResultSetMetaData metaData;

    public SqormCursor(ResultSet rs) throws SQLException {
        this.resultSet = rs;
        this.metaData = rs.getMetaData();
    }

    @Override
    public T next() {
        try {
            if ( !resultSet.next() ) {
                return null;
            }
            String className = resultSet.getString( 1 );
            Class<?> clazz = Class.forName( className );
            Object record = clazz.newInstance();
            int colCount = metaData.getColumnCount();
            for ( int colIndex = 2; colIndex <= colCount; colIndex++ ) {
                String colName = metaData.getColumnName( colIndex );
                Object val = resultSet.getObject( colIndex );
                Method setter = clazz.getMethod(colName); // TODO: Cache reflection
                setter.invoke(record, val);
            }
            return (T)record;
        } catch (Exception ex) {
            throw new RuntimeException("Error during cursor iteration!", ex);
        }
    }

    @Override
    public boolean hasNext() {
        try {
            return !resultSet.isLast() && !resultSet.isAfterLast();
        } catch (SQLException ex) {
            throw new RuntimeException("Error in cursor!", ex);
        }
    }

}
