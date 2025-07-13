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
                    // language=sql
                    """
                    SELECT * FROM publisher;
                    """
                );
            ) {
                while(result.next()) {
                    String name = result.getString("name");

                    Publisher publisher = new Publisher(name);

                    publishers.add(publisher);
                };
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
                    // language=sql
                    """
                    SELECT * FROM publisher WHERE name = ?;
                    """
                );
            ) {
                statement.setString(1, name);
                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
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
                    // language=sql
                    """
                    INSERT INTO publisher (name) VALUES (?);
                    """
                );
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
                    // language=sql
                    """
                    DELETE FROM publisher WHERE name = ?;
                    """
                );
            ) {
                statement.setString(1, name);
                statement.executeUpdate();
            };
        });
    }

    @Override
    public List<Publisher> searchByName(String name) {
        return this.queryOrDefault((connection) -> {
            List<Publisher> publishers = new ArrayList<>();

            try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT *, 
                    (
                        MATCH(name) AGAINST(? IN NATURAL LANGUAGE MODE)
                        + (MATCH(name) AGAINST(? IN BOOLEAN MODE) * 1.5)
                    )
                    AS score
                    FROM publisher
                    ORDER BY score DESC
                    LIMIT 8;
                    """
                );
            ) {
                preparedStatement.setString(1, name);
                if(name.trim().equals("")) preparedStatement.setString(2, "");
                else {
                    StringBuilder builder = new StringBuilder();
                    String[] words = name.split(" ");
                    for(int i = 0; i < words.length; i++) {
                        String word = words[i];
                        if(i > 0) builder.append(" ");
                        builder.append("+");
                        builder.append(word);
                        builder.append("*");
                    };

                    preparedStatement.setString(2, builder.toString());
                };

                try (ResultSet result = preparedStatement.executeQuery()) {
                    while(result.next()) {
                        String completeName = result.getString("name");
                        Publisher publisher = new Publisher(completeName);

                        publishers.add(publisher);
                    };
                };
            };
            
            return publishers;
        }, new ArrayList<>());
    };

    @Override
    public void deleteIfNotUsedByName(String name) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    DELETE FROM publisher WHERE name = ?
                    AND 0 = (
                        SELECT COUNT(*) FROM book
                        WHERE publisher = ?
                    );
                    """
                );
            ) {
                statement.setString(1, name);
                statement.setString(2, name);
                statement.executeUpdate();
            };
        });
    };
};
