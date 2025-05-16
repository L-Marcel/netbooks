package app.netbooks.backend.repositories;

import app.netbooks.backend.connections.Database;
import jakarta.annotation.PostConstruct;

public abstract class BaseRepository {
    protected Database database;

    public BaseRepository(Database database) {
        this.database = database;
    };

    @PostConstruct
    private void postConstruct() {
        this.initialize();
    };

    public void initialize() {};
};
