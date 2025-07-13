package app.netbooks.backend.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Classification;
import app.netbooks.backend.repositories.interfaces.ClassificationsRepository;

@Repository
public class ClassificationsRepositoryImpl extends BaseRepository implements ClassificationsRepository {
    public ClassificationsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public void createOrUpdate(Classification classification) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO classification (book, user, value) VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE value = ?;
                    """
                );
            ) {
                statement.setLong(1, classification.getBook());
                statement.setString(2, classification.getUser().toString());
                statement.setInt(3, classification.getValue());
                statement.setInt(4, classification.getValue());
                statement.executeUpdate();
            };
        });
    };

    @Override
    public Optional<Classification> findByBookAndUser(Long book, UUID user) {
        return this.queryOrDefault((connection) -> {
            Optional<Classification> classificationFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM classification
                    WHERE book = ? AND user = ?;
                    """
                );
            ) {
                statement.setLong(1, book);
                statement.setString(2, user.toString());

                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        Integer value = result.getInt("value");

                        Classification classification = new Classification(
                            book, 
                            user, 
                            value
                        );

                        classificationFound = Optional.of(classification);
                    };
                };
            };

            return classificationFound;
        }, Optional.empty());
    };
};