package app.netbooks.backend.repositories;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;

@Repository
public abstract class BaseRepository {
    protected Database database;

    public BaseRepository(Database database) {
        this.database = database;
        this.initialize();
    };

    abstract public void initialize();
};
