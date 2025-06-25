package app.netbooks.backend.connections.interfaces;

import java.sql.SQLException;
import java.util.function.Supplier;

import app.netbooks.backend.errors.HttpError;

public interface Database {
    public <T> T transaction(
        int transactionLeve,
        Supplier<T> operations,
        Runnable onRollback
    ) throws HttpError;

    public <T> T query(
        QueryFunction<T> query
    ) throws SQLException;

    public void execute(
        OperationFunction operation
    ) throws SQLException;
};
