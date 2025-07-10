package app.netbooks.backend.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Tag;
import app.netbooks.backend.repositories.interfaces.BooksTagsRepository;

@Repository
public class BooksTagsRepositoryImpl extends BaseRepository implements BooksTagsRepository {
    public BooksTagsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Map<Long, List<Tag>> mapAllByBook() {
        return this.queryOrDefault((connection) -> {
            Map<Long, List<Tag>> tagsMap = new LinkedHashMap<>();

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    // language=sql
                    """
                    SELECT * FROM book_tag;        
                    """
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
            };

            return tagsMap;
        }, new LinkedHashMap<>());
    };

    @Override
    public List<Tag> findAllByBook(Long id) {
        return this.queryOrDefault((connection) -> {
            List<Tag> tags = new LinkedList<>();

            try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT tag FROM book_tag WHERE book = ?;        
                    """
                );
            ) {
                preparedStatement.setLong(1, id);
                
                try (ResultSet result = preparedStatement.executeQuery()) {
                    while(result.next()) {
                        String tagName = result.getString("tag");
                        Tag tag = new Tag(tagName);
                        tags.add(tag);
                    };
                };
            };

            return tags;
        }, new LinkedList<>());
    }

    @Override
    public void createMany(List<Tag> tags, Long book) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO book_tag (tag, book) values (?, ?);
                    """
                );
            ) {
                for(Tag tag : tags) {
                    statement.setString(1, tag.getName());
                    statement.setLong(2, book);
                    statement.addBatch();
                };

                statement.executeBatch();
            };
        });
    };
};
