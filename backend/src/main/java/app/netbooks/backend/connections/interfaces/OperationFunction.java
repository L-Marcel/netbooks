package app.netbooks.backend.connections.interfaces;

import java.sql.SQLException;

import app.netbooks.backend.connections.DatabaseConnection;

@FunctionalInterface
public interface OperationFunction {
    public void apply(DatabaseConnection connection) 
        throws SQLException;
};
