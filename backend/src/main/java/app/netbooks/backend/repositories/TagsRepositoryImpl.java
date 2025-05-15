package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.models.Tag;

@Repository
public class TagsRepositoryImpl extends BaseRepository implements TagsRepository{
    public TagsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public List<Tag> findAll() {
        List<Tag> tags = new ArrayList<Tag>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT name FROM tag;"); 
        ) {
            while (result.next()) {
                String name = result.getString("name");
                Tag tag = new Tag(name);
                tags.add(tag);
            };
        } catch (SQLException e) {};

        return tags;
    };

    @Override
    public Optional<Tag> findByName(String name) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT name FROM tag WHERE name = ?"
            );
        ) {
            statement.setString(1, name);
            try (
                ResultSet result = statement.executeQuery();
            ) {
                Optional<Tag> tagFound = Optional.empty();
                
                if (result.next()) {
                    Tag tag = new Tag(name);
                    tagFound = Optional.of(tag);
                };

                return tagFound;
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    };

    @Override
    public void create(Tag tag) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO tag (name) values (?);"
            );
        ) {
            statement.setString(1, tag.getName());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new InternalServerError();
        }
    };

    @Override
    public void deleteByName(String name) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM tag WHERE name = ?;"
            );
        ) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new InternalServerError();
        }
    };
};
