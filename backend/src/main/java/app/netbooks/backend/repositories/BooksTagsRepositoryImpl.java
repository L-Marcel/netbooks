package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.models.Tag;
import app.netbooks.backend.repositories.interfaces.BooksTagsRepository;

@Repository
public class BooksTagsRepositoryImpl extends BaseRepository implements BooksTagsRepository {
    public BooksTagsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Map<Long, List<Tag>> mapAllByBook() {
        Map<Long, List<Tag>> tagsMap = new LinkedHashMap<>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM book_tag;"
            );
        ) {
            while(result.next()) {
                Long book = result.getLong("book");
                String tagName = result.getString("tag");
                Tag tag = new Tag(tagName);

                tagsMap.computeIfAbsent(
                    book,
                    v -> new ArrayList<Tag>()
                ).add(tag);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return tagsMap;
    };

    @Override
    public List<Tag> findAllByBook(Long id) {
        List<Tag> tags = new LinkedList<>();

        try (
            Connection connection = this.database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT tag FROM book_tag WHERE book = ?;"
            );
        ) {
            preparedStatement.setLong(1, id);
            
            try (
                ResultSet result = preparedStatement.executeQuery();
            ) {
                while(result.next()) {
                    String tagName = result.getString("tag");
                    Tag tag = new Tag(tagName);
                    tags.add(tag);
                };
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return tags;
    };
};
