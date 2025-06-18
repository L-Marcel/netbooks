package app.netbooks.backend.connections.interfaces;

import java.sql.SQLException;

import app.netbooks.backend.connections.DatabaseConnection;

@FunctionalInterface
public interface QueryFunction<T> {
    public T apply(DatabaseConnection connection) 
        throws SQLException;
};
