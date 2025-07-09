package app.netbooks.backend.connections.transactions;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.errors.HttpError;
import lombok.Getter;
import lombok.Setter;

@Component
public class Transactions {
    @Autowired
    private Database database;

    @Getter
    @Setter
    private TransactionIsolationLevel isolationLevel = TransactionIsolationLevel.REPEATABLE_READ;
    
    public <T> T run(
        Supplier<T> operations,
        Runnable onRollback
    ) throws HttpError {
        return this.database.transaction(
            this.isolationLevel.getValue(), 
            operations,
            onRollback
        );
    };

    public <T> T run(
        Supplier<T> operations
    ) throws HttpError {
        return this.run(operations, () -> {});
    };

    public <T> T run(
        Runnable operations,
        Runnable onRollback
    ) throws HttpError {
        return this.run(
            () -> {
                operations.run();
                return null;
            },
            onRollback
        );
    };

    public <T> T run(
        Runnable operations
    ) throws HttpError {
        return this.run(operations, () -> {});
    };
};
