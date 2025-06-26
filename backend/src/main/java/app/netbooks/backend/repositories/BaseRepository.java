package app.netbooks.backend.repositories;

import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.connections.interfaces.OperationFunction;
import app.netbooks.backend.connections.interfaces.QueryFunction;
import app.netbooks.backend.errors.InternalServerError;
import jakarta.annotation.PostConstruct;

public abstract class BaseRepository {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        Function<Exception, T> expcetion
    ) {
        try {
            return this.database.query(query);
        } catch (Exception e) {
            this.logger.debug(e.getMessage());
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
    ) {
        return this.query(query, (e) -> {
            this.logger.debug(e.getMessage());
            if(e instanceof SQLException)
                throw new InternalServerError((SQLException) e);
            else throw new InternalServerError();
        });
    };

    public void execute(
        OperationFunction operation,
        Consumer<Exception> expcetion
    ) {
        try {
            this.database.execute(operation);
        } catch (Exception e) {
            this.logger.debug(e.getMessage());
            expcetion.accept(e);
        }
    };

    public void execute(
        OperationFunction operation
    ) {
        this.execute(operation, (e) -> {
            this.logger.debug(e.getMessage());
            if(e instanceof SQLException)
                throw new InternalServerError((SQLException) e);
            else throw new InternalServerError();
        });
    };
};
