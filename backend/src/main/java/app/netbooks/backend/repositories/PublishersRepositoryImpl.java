package app.netbooks.backend.repositories;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Publisher;
import app.netbooks.backend.repositories.interfaces.PublishersRepository;

@Repository
public class PublishersRepositoryImpl extends BaseRepository implements PublishersRepository {
    public PublishersRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public List<Publisher> findAll() {
        return this.queryOrDefault((connection) -> {
            List<Publisher> publishers = new ArrayList<>();

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    "SELECT * FROM publisher;"
                );
            ) {
                while (result.next()) {
                    String name = result.getString("name");

                    Publisher publisher = new Publisher(name);

                    publishers.add(publisher);
                }
            };
            
            return publishers;
        }, new ArrayList<>());
    };

    @Override
    public Optional<Publisher> findByName(String name) {
        return this.queryOrDefault((connection) -> {
            Optional<Publisher> publisherFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM publisher WHERE name = ?;"
                )
            ) {
                statement.setString(1, name);
                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        String resultName = result.getString("name");

                        Publisher publisher = new Publisher(resultName);

                        publisherFound = Optional.of(publisher);
                    };
                };
            };

            return publisherFound;
        }, Optional.empty());
    };

    @Override
    public void create(Publisher publisher) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO publisher (name) VALUES (?);"
                )
            ) {
                statement.setString(1, publisher.getName());
                statement.executeUpdate();
            };
        });
    };

    @Override
    public void deleteByName(String name) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM publisher WHERE name = ?;"
                )
            ) {
                statement.setString(1, name);
                statement.executeUpdate();
            };
        });
    };
};
