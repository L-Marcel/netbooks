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
    public Optional<Author> findById(Long id) {
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
                statement.setLong(1, id);
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
                    SELECT *, 
                    (
                        MATCH(name) AGAINST(? IN NATURAL LANGUAGE MODE)
                        + (MATCH(name) AGAINST(? IN BOOLEAN MODE) * 1.5)
                    )
                    AS score
                    FROM author
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
                        Long id = result.getLong("id");
                        String completeName = result.getString("name");
                        Double score = result.getDouble("score");
                        Author author = new Author(id, completeName, score);
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
    };

    @Override
    public void deleteById(Long id) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    DELETE FROM author WHERE id = ?;
                    """
                )
            ) {
                statement.setLong(1, id);
                statement.executeUpdate();            
            }
        });
    };

    @Override
    public void createMany(List<Author> authors) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO author (name) VALUES (?);
                    """,
                    true
                );
            ) {
                for(Author author : authors) {
                    statement.setString(1, author.getName());
                    statement.addBatch();
                };

                statement.executeBatch();
                
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    for (Author author : authors) {
                        generatedKeys.next();
                        author.setId(generatedKeys.getLong(1));
                    };
                };
            };
        });
    }

    @Override
    public void deleteManyIfNotUsedById(List<Long> ids) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    DELETE FROM author WHERE id = ?
                    AND 0 = (
                        SELECT COUNT(*) FROM book_author
                        WHERE author = ?
                    );
                    """
                );
            ) {
                for(Long id : ids) {
                    statement.setLong(1, id);
                    statement.setLong(2, id);
                    statement.addBatch();
                };

                statement.executeBatch();
            };
        });
    };
};
