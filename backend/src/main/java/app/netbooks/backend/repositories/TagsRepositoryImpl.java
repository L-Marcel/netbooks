package app.netbooks.backend.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Tag;
import app.netbooks.backend.repositories.interfaces.TagsRepository;

@Repository
public class TagsRepositoryImpl extends BaseRepository implements TagsRepository {
    public TagsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public List<Tag> findAll() {
        return this.queryOrDefault((connection) -> {
            List<Tag> tags = new ArrayList<>();

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    "SELECT * FROM tag;"
                ); 
            ) {
                while (result.next()) {
                    String name = result.getString("name");
                    Tag tag = new Tag(name);
                    tags.add(tag);
                };
            };

            return tags;
        }, new ArrayList<>());
    };

    @Override
    public Optional<Tag> findByName(String name) {
        return this.queryOrDefault((connection) -> {
            Optional<Tag> tagFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM tag WHERE name = ?;"
                );
            ) {
                statement.setString(1, name);
                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        Tag tag = new Tag(name);
                        tagFound = Optional.of(tag);
                    };
                };
            };

            return tagFound;
        }, Optional.empty());
    };

    @Override
    public void create(Tag tag) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO tag (name) values (?);"
                );
            ) {
                statement.setString(1, tag.getName());
                statement.executeUpdate();
            };
        });
    };

    @Override
    public void deleteByName(String name) {
        this.execute((connection) -> {
           try (
                PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM tag WHERE name = ?;"
                );
            ) {
                statement.setString(1, name);
                statement.executeUpdate();
            }; 
        });
    };
};
