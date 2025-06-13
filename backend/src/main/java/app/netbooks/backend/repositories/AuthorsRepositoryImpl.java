package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.models.Author;
import app.netbooks.backend.repositories.interfaces.AuthorRepository;

@Repository
public class AuthorsRepositoryImpl extends BaseRepository implements AuthorRepository {
    public AuthorsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Optional<Author> findById(Integer id) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
              "SELECT * FROM author WHERE id = ?;"  
            );
        ) {
            statement.setInt(1, id);
            try (
                ResultSet result = statement.executeQuery();
            ) {
                Optional<Author> authorFound = Optional.empty();
                if (result.next()) {
                    String name = result.getString("name");
                    Author author = new Author(id, name);
                    authorFound = Optional.of(author);
                };

                return authorFound;
            }
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }
    
    @Override
    public List<Author> searchByName(String name) {
        List<Author> authors = new ArrayList<Author>();
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM author WHERE name LIKE ?;"
            );
        ) {
            preparedStatement.setString(1, "%" + name + "%");
            try (
                ResultSet result = preparedStatement.executeQuery();
            ) {
                while (result.next()) {
                    Integer id = result.getInt("id");
                    String completeName = result.getString("name");
                    Author author = new Author(id, completeName);
                    authors.add(author);
                }
            }
        } catch (SQLException e) {
            throw new InternalServerError();
        }

        return authors;
    }

    @Override
    public void create(Author author) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO author (name) VALUES (?);"
            )
        ) {
           statement.setString(1, author.getName());
           statement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM author WHERE id = ?;"
            )
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();            
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }
    
}
