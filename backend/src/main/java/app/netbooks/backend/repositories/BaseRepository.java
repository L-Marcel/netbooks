package app.netbooks.backend.repositories;

import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.connections.interfaces.OperationFunction;
import app.netbooks.backend.connections.interfaces.QueryFunction;
import app.netbooks.backend.errors.InternalServerError;
import jakarta.annotation.PostConstruct;

public abstract class BaseRepository {
    private Database database;

    public BaseRepository(Database database) {
        this.database = database;
    };

    @PostConstruct
    private void postConstruct() {
        this.initialize();
    };

    public void initialize() {};

    public <T> T query(
        QueryFunction<T> query,
        Function<SQLException, T> expcetion
    ) {
        try {
            return this.database.query(query);
        } catch (SQLException e) {
            return expcetion.apply(e);
        }
    };

    public <T> T queryOrDefault(
        QueryFunction<T> query,
        T defaultValue
    ) {
        return this.query(query, (e) -> defaultValue);
    };

    public <T> T query(
        QueryFunction<T> query
    ) throws InternalServerError {
        return this.query(query, (e) -> {
            throw new InternalServerError();
        });
    };

    public void execute(
        OperationFunction operation,
        Consumer<SQLException> expcetion
    ) {
        try {
            this.database.execute(operation);
        } catch (SQLException e) {
            expcetion.accept(e);
        }
    };

    public void execute(
        OperationFunction operation
    ) throws InternalServerError {
        this.execute(operation, (e) -> {
            throw new InternalServerError();
        });
    };
};
