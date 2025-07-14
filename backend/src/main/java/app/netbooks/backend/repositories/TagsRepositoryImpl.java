package app.netbooks.backend.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Tag;
import app.netbooks.backend.repositories.interfaces.TagsRepository;

@Repository
public class TagsRepositoryImpl extends BaseRepository implements TagsRepository {
    public TagsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public List<Tag> findAll() {
        return this.queryOrDefault((connection) -> {
            List<Tag> tags = new ArrayList<>();

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    // language=sql
                    """
                    SELECT * FROM tag;
                    """
                ); 
            ) {
                while(result.next()) {
                    String name = result.getString("name");
                    Tag tag = new Tag(name);
                    tags.add(tag);
                };
            };

            return tags;
        }, new ArrayList<>());
    };

    @Override
    public Optional<Tag> findByName(String name) {
        return this.queryOrDefault((connection) -> {
            Optional<Tag> tagFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM tag WHERE name = ?;
                    """
                );
            ) {
                statement.setString(1, name);
                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        Tag tag = new Tag(name);
                        tagFound = Optional.of(tag);
                    };
                };
            };

            return tagFound;
        }, Optional.empty());
    };

    @Override
    public void create(Tag tag) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO tag (name) VALUES (?);
                    """
                );
            ) {
                statement.setString(1, tag.getName());
                statement.executeUpdate();
            };
        });
    };

    @Override
    public void createMany(List<Tag> tags) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT IGNORE INTO tag (name) VALUES (?);
                    """
                );
            ) {
                for(Tag tag : tags) {
                    statement.setString(1, tag.getName());
                    statement.addBatch();
                };

                statement.executeBatch();
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
                    DELETE FROM tag WHERE name = ?;
                    """
                );
            ) {
                statement.setString(1, name);
                statement.executeUpdate();
            }; 
        });
    }

    @Override
    public List<Tag> searchByName(String name) {
        return this.queryOrDefault((connection) -> {
            List<Tag> tags = new ArrayList<>();

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
                    FROM tag
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
                        Tag tag = new Tag(completeName);
                        tags.add(tag);
                    };
                };
            };
            
            return tags;
        }, new ArrayList<>());
    }

    @Override
    public void deleteManyIfNotUsedByName(List<String> names) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    DELETE FROM tag WHERE name = ? 
                    AND 0 = (
                        SELECT COUNT(*) FROM book_tag
                        WHERE tag = ?
                    );
                    """
                );
            ) {
                for(String name : names) {
                    statement.setString(1, name);
                    statement.setString(2, name);
                    statement.addBatch();
                };

                statement.executeBatch();
            };
        });
    };

    @Override
    public List<Tag> searchRandomTags(int limit) {
        return this.queryOrDefault((connection) -> {
            List<Tag> tags = new ArrayList<>();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM tag ORDER BY RAND() LIMIT ?;
                    """
                );
            ) {
                statement.setInt(1, limit);

                try (ResultSet result = statement.executeQuery()) {
                    while(result.next()) {
                        String name = result.getString("name");
                        Tag tag = new Tag(name);
                        tags.add(tag);
                    }
                }
            }

            return tags;
        }, new ArrayList<>());
    };

};
