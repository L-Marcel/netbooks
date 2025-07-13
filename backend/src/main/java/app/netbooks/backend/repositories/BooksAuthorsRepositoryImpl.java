package app.netbooks.backend.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Author;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.repositories.interfaces.BooksAuthorsRepository;

@Repository
public class BooksAuthorsRepositoryImpl extends BaseRepository implements BooksAuthorsRepository {
    public BooksAuthorsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Map<Long, List<Author>> mapAllByBooks() {
        return this.queryOrDefault((connection) -> {
            Map<Long, List<Author>> authorsMap = new LinkedHashMap<>();

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    // language=sql
                    """
                    SELECT * FROM book_authors;
                    """
                );
            ) {
                while(result.next()) {
                    Long book = result.getLong("book");
                    Long authorId = result.getLong("author");
                    String name = result.getString("name");
                    Author author = new Author(authorId, name);

                    authorsMap.computeIfAbsent(
                        book,
                        v -> new ArrayList<Author>()
                    ).add(author);
                };
            };

            return authorsMap;
        }, new LinkedHashMap<>());
    };

    @Override
    public Map<Long, List<Author>> mapAllByBooks(List<Book> books) {
        return this.queryOrDefault((connection) -> {
            Map<Long, List<Author>> authorsMap = new LinkedHashMap<>();

            String ids = books.stream().map((book) -> book.getId().toString()).collect(Collectors.joining(", "));

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    // language=sql
                    """
                    SELECT * FROM book_author AS bat
                    JOIN author AS aut
                    ON bat.author = aut.id
                    WHERE book IN
                    """
                    + " (" + ids + ");"
                );
            ) { 
                while(result.next()) {
                    Long book = result.getLong("book");
                    Long id = result.getLong("author");
                    String name = result.getString("name");
                    Author author = new Author(id, name);

                    authorsMap.computeIfAbsent(
                        book,
                        v -> new ArrayList<Author>()
                    ).add(author);
                };
            };

            return authorsMap;
        }, new LinkedHashMap<>());
    };

    @Override
    public List<Author> findAllByBook(Long id) {
        return this.queryOrDefault((connection) -> {
            List<Author> authors = new LinkedList<>();

            try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM book_authors WHERE book = ?;
                    """
                );
            ) {
                preparedStatement.setLong(1, id);
                
                try (ResultSet result = preparedStatement.executeQuery()) {
                    while(result.next()) {
                        Long authorId = result.getLong("author");
                        String name = result.getString("name");
                        
                        Author author = new Author(authorId, name);

                        authors.add(author);
                    };
                };    
            };

            return authors;
        }, new LinkedList<>());
    }

    @Override
    public void createMany(List<Author> authors, Long book) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO book_author (author, book) VALUES (?, ?);
                    """
                );
            ) {
                for(Author author : authors) {
                    statement.setLong(1, author.getId());
                    statement.setLong(2, book);
                    statement.addBatch();
                };
                
                statement.executeBatch();
            };
        });
    };

    @Override
    public void deleteByBook(Long book) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    DELETE FROM book_author WHERE book = ?;
                    """
                )
            ) {
                statement.setLong(1, book);
                statement.executeUpdate();            
            }
        });
    }
};