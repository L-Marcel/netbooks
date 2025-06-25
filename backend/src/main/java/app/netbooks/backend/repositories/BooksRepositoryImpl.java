package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
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
        List<Book> books = new ArrayList<Book>();

        try (
            Connection connection = this.database.getConnection();
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
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return books;
    };

    @Override
    public Optional<Book> findById(Long id) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM book WHERE id = ?;"
            );
        ){
            statement.setObject(1, id);
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
        } catch (SQLException e) {
            return Optional.empty();
        }
    };
};
