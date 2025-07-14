package app.netbooks.backend.repositories;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.models.Publisher;
import app.netbooks.backend.repositories.interfaces.BooksRepository;

@Repository
public class BooksRepositoryImpl extends BaseRepository implements BooksRepository {
    public BooksRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public List<Book> findAll() {
        return this.queryOrDefault((connection) -> {
            List<Book> books = new ArrayList<Book>();

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    // language=sql
                    """
                    SELECT * FROM book_with_stars;     
                    """
                );
            ) {
                while(result.next()) {
                    Long id = result.getLong("id");
                    Long isbn = result.getLong("isbn");
                    String title = result.getString("title");
                    String description = result.getString("description");
                    Double stars = result.getDouble("stars");
                    Integer numPages = result.getInt("num_pages");
                    Date publishedIn = result.getDate("published_in");

                    String publisherName = result.getString("publisher");
                    Publisher publisher = new Publisher(publisherName);

                    Book book = new Book(
                        id,
                        isbn,
                        title,
                        description,
                        stars,
                        numPages,
                        publishedIn,
                        publisher
                    );

                    books.add(book);
                };
            };

            return books;
        }, new ArrayList<>());
    };

    @Override
    public Optional<Book> findById(Long id) {
        return this.queryOrDefault((connection) -> {
            Optional<Book> bookFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM book_with_stars WHERE id = ?;
                    """
                );
            ) {
                statement.setLong(1, id);
                try(ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        Long isbn = result.getLong("isbn");
                        String title = result.getString("title");
                        String description = result.getString("description");
                        Double stars = result.getDouble("stars");
                        Integer numPages = result.getInt("num_pages");
                        Date publishedIn = result.getDate("published_in");
                    
                        String publisherName = result.getString("publisher");
                        Publisher publisher = new Publisher(publisherName);

                        Book book = new Book(
                            id,
                            isbn,
                            title,
                            description,
                            stars,
                            numPages,
                            publishedIn,
                            publisher
                        );

                        bookFound = Optional.of(book);
                    };
                };
            };

            return bookFound;
        }, Optional.empty());
    };

    @Override
    public Optional<Book> findByIsbn(Long isbn) {
        return this.queryOrDefault((connection) -> {
            Optional<Book> bookFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM book_with_stars 
                    WHERE isbn = ? AND isbn IS NOT NULL;
                    """
                );
            ) {
                statement.setLong(1, isbn);

                try(ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        Long id = result.getLong("id");
                        String title = result.getString("title");
                        String description = result.getString("description");
                        Double stars = result.getDouble("stars");
                        Integer numPages = result.getInt("num_pages");
                        Date publishedIn = result.getDate("published_in");
                    
                        String publisherName = result.getString("publisher");
                        Publisher publisher = new Publisher(publisherName);

                        Book book = new Book(
                            id,
                            isbn,
                            title,
                            description,
                            stars,
                            numPages,
                            publishedIn,
                            publisher
                        );

                        bookFound = Optional.of(book);
                    };
                };
            };

            return bookFound;
        }, Optional.empty());
    };

    @Override
    public List<Book> findBooksWithAllTags(List<String> tagNames, int size) {
        return this.queryOrDefault((connection) -> {
            List<Book> books = new ArrayList<>();

            if (tagNames == null || tagNames.isEmpty())
                return books;

            String placeholders = tagNames.stream()
                .map(t -> "?")
                .collect(Collectors.joining(", "));

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT b.*
                    FROM book_with_stars b
                    JOIN book_tag bt ON b.id = bt.book
                    JOIN tag t ON bt.tag = t.name
                    WHERE t.name IN (""" + placeholders + ") " + 
                    """
                    GROUP BY b.id, b.isbn, b.title, b.description, b.stars, b.num_pages, b.published_in, b.publisher
                    HAVING COUNT(DISTINCT t.name) = ?
                    """
                );
            ) {
                int index = 1;
                for (String tag : tagNames) {
                    statement.setString(index++, tag);
                }

                statement.setInt(index, size);

                try (ResultSet result = statement.executeQuery()) {
                    while (result.next()) {
                        Long id = result.getLong("id");
                        Long isbn = result.getLong("isbn");
                        String title = result.getString("title");
                        String description = result.getString("description");
                        Double stars = result.getDouble("stars");
                        Integer numPages = result.getInt("num_pages");
                        Date publishedIn = result.getDate("published_in");

                        String publisherName = result.getString("publisher");
                        Publisher publisher = new Publisher(publisherName);

                        Book book = new Book(
                            id,
                            isbn,
                            title,
                            description,
                            stars,
                            numPages,
                            publishedIn,
                            publisher
                        );

                        books.add(book);
                    }
                }
            }

            return books;
        }, new ArrayList<>());
    }


    @Override
    public void create(Book book) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO book (isbn, title, description, num_pages, published_in, publisher)
                    VALUES (?, ?, ?, ?, ?, ?);
                    """,
                    true
                );
            ) {
                if(book.getIsbn() != null) statement.setLong(1, book.getIsbn());
                else statement.setNull(1, java.sql.Types.BIGINT);
                
                statement.setString(2, book.getTitle());
                statement.setString(3, book.getDescription());
                statement.setInt(4, book.getNumPages());
                statement.setDate(5, book.getPublishedIn());
                statement.setString(6, book.getPublisher().getName());
                statement.executeUpdate();

                try (ResultSet result = statement.getGeneratedKeys()) {
                    result.next();
                    Long id = result.getLong(1);
                    book.setId(id);
                };
            };
        });
    };

    @Override
    public void deleteById(Long id) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    DELETE FROM book WHERE id = ?;
                    """
                );
            ) {
                statement.setLong(1, id);
                statement.executeUpdate();
            };
        });
    };

    @Override
    public void update(Book book) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE book
                    SET isbn = ?, 
                        title = ?, 
                        description = ?, 
                        num_pages = ?, 
                        published_in = ?, 
                        publisher = ?
                    WHERE id = ?;
                    """
                );
            ) {
                statement.setLong(1, book.getIsbn());
                statement.setString(2, book.getTitle());
                statement.setString(3, book.getDescription());
                statement.setInt(4, book.getNumPages());
                statement.setDate(5, book.getPublishedIn());
                statement.setString(6, book.getPublisher().getName());
                statement.setLong(7, book.getId());
                statement.executeUpdate();
            };
        });
    }
};
