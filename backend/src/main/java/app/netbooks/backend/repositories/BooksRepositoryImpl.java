package app.netbooks.backend.repositories;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                    "SELECT * FROM book_with_stars;"
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
        }, new ArrayList<Book>());
    };

    @Override
    public Optional<Book> findById(Long id) {
        return this.queryOrDefault((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM book WHERE id = ?;"
                );
            ) {
                statement.setLong(1, id);
                try(ResultSet result = statement.executeQuery()) {
                    Optional<Book> bookFound = Optional.empty();

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
                    
                    return bookFound;
                }
            }
        }, Optional.empty());
    };
};
