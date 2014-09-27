package net.squarelabs.sqorm;

import net.squarelabs.sqorm.schema.ColumnSchema;
import net.squarelabs.sqorm.schema.DbSchema;
import net.squarelabs.sqorm.schema.TableSchema;
import net.squarelabs.sqorm.sql.TypeConverter;

import java.sql.*;
import java.util.Iterator;

/**
 * BEWARE: This class is basically centered around a convenient impedance mismatch: Iterator expects that it is able to
 * non-destructively check that there is a next record (hasNext). A JDBC Statement makes no such assumption - the check
 * is deferred:
 *   execute() returns false if there is no result
 *   getResultSet() returns null if there is no first record
 *   getMoreResults() may return true or false, but changes the current state either way
 *
 * However, this class only exists to appease the Java for-each statement. The for-each statement always calls hasNext()
 * then next(). hasNext() is meant not to be non-destructive, and next() is meant to advance the state. However, this
 * class takes advantage of the fact that hasNext() is always called, and uses that method to change the state. next()
 * is actually a stateless operation.
 *
 * To reiterate: This class is only meant to be used by a for-each operator, and relying on the hasNext() to be
 * non-destructive is not safe!
 *
 * It is very likely that this class will be refactored or deprecated.
 */
public class Cursor implements Iterable<Object>, Iterator<Object> {

    private boolean iterated = false;
    private DbSchema db;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private PreparedStatement stmt;

    public Cursor(DbSchema db, PreparedStatement stmt) throws SQLException {
        this.db = db;
        this.stmt = stmt;
    }

    /**
     * @return Despite its name, this method does not advance - it merely returns the current record
     */
    @Override
    public Object next() {
        try {
            String className = resultSet.getString(1);
            Class<?> clazz = Class.forName(className);
            TableSchema table = db.getTable(clazz);
            if(table == null) {
                throw new RuntimeException("Table not found: " + clazz.getCanonicalName());
            }
            Object record = clazz.newInstance();
            int colCount = metaData.getColumnCount();
            for (int colIndex = 2; colIndex <= colCount; colIndex++) {
                Object sqlVal = resultSet.getObject(colIndex);
                String colName = metaData.getColumnLabel(colIndex);
                ColumnSchema col = table.getColumn(colName);
                if(col == null) {
                    throw new Exception("Column [" + colName + "] not found on table " + className);
                }
                Object javaVal;
                try {
                    javaVal = TypeConverter.sqlToJava(col.getType(), sqlVal);
                } catch (Exception ex) {
                    throw new RuntimeException("Error converting [" + table.getName() + "].[" + colName + "]", ex);
                }
                col.set(record, javaVal);
            }
            return record;
        } catch (Exception ex) {
            throw new RuntimeException("Error during cursor iteration!", ex);
        }
    }

    /**
     * @return Despite its name, this method doesn't just check if there are more results - it actually advances to the
     * next result, and returns true if there is one, and false if there is not
     */
    @Override
    public boolean hasNext() {
        try {
            // If there is no ResultSet yet, get the first one
            if(resultSet == null) {
                resultSet = stmt.getResultSet();
            }

            // Keep trying ResultSets until we find one with at least one record
            while(!resultSet.next()) {
                if(!stmt.getMoreResults()) {
                    return false; // Bail if there are no more ResultSets
                }
                resultSet = stmt.getResultSet();
            }

            // Once we've got a ResultSet, cache the meta data
            metaData = resultSet.getMetaData();
            return true;
        } catch (Exception ex) {
            throw new RuntimeException("Error getting ResultSet!", ex);
        }
    }

    @Override
    public synchronized Iterator<Object> iterator() {
        if(iterated) {
            throw new RuntimeException("Cursor has already been exhausted!");
        }
        iterated = true;
        return this;
    }
}
