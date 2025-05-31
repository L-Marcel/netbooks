package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.models.Publisher;

@Repository
public class PublisherRepositoryImpl extends BaseRepository implements PublisherRepository{

    public PublisherRepositoryImpl(Database database) {
        super(database);
    }

    @Override
    public List<Publisher> findAll() {
        List<Publisher> publishers = new ArrayList<Publisher>();
        try (
            Connection connection = this.database.getConnection();
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
        } catch (SQLException e) {
            throw new InternalServerError();
        }
        return publishers;
    }

    @Override
    public Optional<Publisher> find(String name) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM publisher WHERE name = ?;"
            )
        ) {
            statement.setString(1, name);
            try (
                ResultSet result = statement.executeQuery();
            ) {
                Optional<Publisher> publisherFound = Optional.empty();
                if (result.next()) {
                    String resultName = result.getString("name");
                    Publisher publisher = new Publisher(resultName);
                    publisherFound = Optional.of(publisher);
                }
                
                return publisherFound;
            }
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }

    @Override
    public void create(Publisher publisher) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO publisher (name) VALUES (?);"
            )
        ) {
            statement.setString(1, publisher.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }

    @Override
    public void deleteByName(String name) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM publisher WHERE name = ?;"
            )
        ) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }
    
}
