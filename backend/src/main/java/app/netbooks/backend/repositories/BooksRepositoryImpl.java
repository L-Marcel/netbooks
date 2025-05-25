package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.models.Author;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.models.Publisher;
import app.netbooks.backend.models.Tag;

@Repository
public class BooksRepositoryImpl extends BaseRepository implements BooksRepository, BooksTagsRepository, BooksAuthorsRepository {
    public BooksRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Map<Long, List<Author>> mapAllBookAuthors() {
        Map<Long, List<Author>> authorsMap = new LinkedHashMap<>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM book_authors;"
            );
        ) {
            while(result.next()) {
                Long id = result.getLong("book");
                Integer authorId = result.getInt("author");
                String name = result.getString("name");
                Author author = new Author(authorId, name);

                authorsMap.computeIfAbsent(
                    id,
                    v -> new ArrayList<Author>()
                ).add(author);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return authorsMap;
    };

    @Override
    public List<Author> findAuthors(Long id) {
        List<Author> authors = new LinkedList<>();

        try (
            Connection connection = this.database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT tag FROM book_authors WHERE book = ?;"
            );
        ) {
            preparedStatement.setLong(1, id);
            
            try (
                ResultSet result = preparedStatement.executeQuery();
            ) {
                while(result.next()) {
                    Integer authorId = result.getInt("author");
                    String name = result.getString("name");
                    Author author = new Author(authorId, name);
                    authors.add(author);
                };
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return authors;
    };

    @Override
    public Map<Long, List<Tag>> mapAllBookTags() {
        Map<Long, List<Tag>> tagsMap = new LinkedHashMap<>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM book_tag;"
            );
        ) {
            while(result.next()) {
                Long id = result.getLong("book");
                String tagName = result.getString("tag");
                Tag tag = new Tag(tagName);

                tagsMap.computeIfAbsent(
                    id,
                    v -> new ArrayList<Tag>()
                ).add(tag);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return tagsMap;
    };

    @Override
    public List<Tag> findTags(Long id) {
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
            Map<Long, List<Tag>> mappedBookTags = this.mapAllBookTags();
            Map<Long, List<Author>> mapedBookAuthors = this.mapAllBookAuthors();
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

                List<Tag> tags = mappedBookTags.getOrDefault(id, new LinkedList<>());
                List<Author> authors = mapedBookAuthors.getOrDefault(id, new LinkedList<>());
                Book book = new Book(
                    id,
                    isbn,
                    title,
                    description,
                    stars,
                    numPages,
                    publishedIn,
                    publisher,
                    authors,
                    tags
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

                    List<Tag> tags = this.findTags(id);
                    List<Author> authors = this.findAuthors(id);
                    Book book = new Book(
                        id,
                        isbn,
                        title,
                        description,
                        stars,
                        numPages,
                        publishedIn,
                        publisher,
                        authors,
                        tags
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
