package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.models.Tag;

@Repository
public class TagsRepositoryImpl extends BaseRepository implements TagsRepository{

    public TagsRepositoryImpl(Database database) {
        super(database);
    }

    @Override
    public void initialize() {
        try {
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();

            statement.execute(
                "CREATE TABLE IF NOT EXISTS Tags (\n" + 
                "    name VARCHAR(20) PRIMARY KEY\n" + 
                ");"
            );

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Tag> findAll() {
        List<Tag> tags = new ArrayList<Tag>();

        try {
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(
                "SELECT name FROM Tags;"
            );

            while (result.next()) {
                String name = result.getString("name");

                Tag tag = new Tag(name);
                tags.add(tag);
            }

            result.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tags;
    }

    @Override
    public void create(Tag tag) {
        try {
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Tags (name) values (?);");

            statement.setString(1, tag.getName());
            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
    
}
