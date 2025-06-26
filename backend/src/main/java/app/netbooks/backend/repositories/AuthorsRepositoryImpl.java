package app.netbooks.backend.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Author;
import app.netbooks.backend.repositories.interfaces.AuthorsRepository;

@Repository
public class AuthorsRepositoryImpl extends BaseRepository implements AuthorsRepository {
    public AuthorsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Optional<Author> findById(Integer id) {
        return this.queryOrDefault((connection) -> {
            Optional<Author> authorFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM author WHERE id = ?;
                    """
                );
            ) {
                statement.setInt(1, id);
                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        String name = result.getString("name");
                        Author author = new Author(id, name);
                        authorFound = Optional.of(author);
                    };
                };
            };

            return authorFound;
        }, Optional.empty());
    };
    
    @Override
    public List<Author> searchByName(String name) {
        return this.queryOrDefault((connection) -> {
            List<Author> authors = new ArrayList<>();

            try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM author WHERE name LIKE ?;
                    """
                );
            ) {
                preparedStatement.setString(1, "%" + name + "%");

                try (ResultSet result = preparedStatement.executeQuery()) {
                    while(result.next()) {
                        Integer id = result.getInt("id");
                        String completeName = result.getString("name");
                        Author author = new Author(id, completeName);
                        authors.add(author);
                    };
                };  
            };

            return authors;
        }, new ArrayList<Author>());
    };

    @Override
    public void create(Author author) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO author (name) VALUES (?);
                    """
                )
            ) {
                statement.setString(1, author.getName());
                statement.executeUpdate();
            }
        });
    }

    @Override
    public void deleteById(Integer id) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    DELETE FROM author WHERE id = ?;
                    """
                )
            ) {
                statement.setInt(1, id);
                statement.executeUpdate();            
            }
        });
    };  
};
