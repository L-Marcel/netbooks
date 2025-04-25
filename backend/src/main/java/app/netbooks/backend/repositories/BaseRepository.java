package app.netbooks.backend.repositories;

import app.netbooks.backend.connections.Database;

public abstract class BaseRepository {
    protected Database database;

    public BaseRepository(Database database) {
        this.database = database;
        this.initialize();
    };

    abstract public void initialize();
};
