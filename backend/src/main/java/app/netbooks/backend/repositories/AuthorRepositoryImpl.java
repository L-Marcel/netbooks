package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.errors.AuthorNotFound;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.models.Author;

@Repository
public class AuthorRepositoryImpl extends BaseRepository implements AuthorRepository{

    public AuthorRepositoryImpl(Database database) {
        super(database);
    }

    @Override
    public Optional<Author> findById(Integer id) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
              "SELECT * FROM author WHERE id = ?;"  
            );
        ) {
            statement.setString(1, String.valueOf(id));
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            throw new InternalServerError();
        }

        return authors;
    }

    @Override
    public void create(Author author) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO author (id, name) VALUES (?, ?);"
            )
        ) {
           statement.setString(1, String.valueOf(author.getId()));
           statement.setString(2, author.getName());
           statement.executeQuery();
        } catch (Exception e) {
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
            statement.setString(1, String.valueOf(id));
            statement.executeQuery();            
        } catch (Exception e) {
            throw new InternalServerError();
        }
    }
    
}
