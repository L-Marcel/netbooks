package app.netbooks.backend.connections;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.connections.interfaces.OperationFunction;
import app.netbooks.backend.connections.interfaces.QueryFunction;
import app.netbooks.backend.errors.HttpError;
import app.netbooks.backend.errors.InternalServerError;

@Component
public class DatabaseImpl implements Database {
    @Autowired
    private Connections connections;

    @Override
    public <T> T transaction(
        int isolationLevel,
        Supplier<T> operations,
        Runnable onRollback
    ) throws HttpError {
        if(connections.haveTransactionRunning()) {
            return operations.get();
        } else {
            try {
                Connection connection = connections.getTransactionConnection();
                connection.setTransactionIsolation(isolationLevel);
                connection.setAutoCommit(false);
                T result = operations.get();
                connection.commit();
                connection.close();
                connections.clearTransactionConnection();
                return result;
            } catch (Exception e) {
                try {
                    Connection connection = connections.getTransactionConnection();
                    connection.rollback();
                    connection.close();
                    connections.clearTransactionConnection();
                    onRollback.run();
                } catch (SQLException e2) {
                    connections.clearTransactionConnection();
                };

                throw new InternalServerError();
            }
        }
    };
    
    @Override
    public <T> T query(
        QueryFunction<T> query
    ) throws SQLException, HttpError {
        if(connections.haveTransactionRunning()) {
            DatabaseConnection connection = new DatabaseConnection(
                connections.getTransactionConnection()
            );

            return query.apply(connection);
        } else {
            try (
                DatabaseConnection connection = new DatabaseConnection(
                    connections.getConnection()
                );
            ) {
                return query.apply(connection);
            } catch (SQLException e) {
                throw new InternalServerError();
            }
        }
    };

    @Override
    public void execute(
        OperationFunction operation
    ) throws SQLException {
        this.query((connection) -> {
            try {
                operation.apply(connection);
            } catch (InternalServerError e) {
                if(e.getSqlException() instanceof SQLException)
                    throw e.getSqlException();
            };

            return null;
        });
    };
};
