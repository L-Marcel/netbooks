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
import app.netbooks.backend.models.Author;
import app.netbooks.backend.repositories.interfaces.BooksAuthorsRepository;

@Repository
public class BooksAuthorsRepositoryImpl extends BaseRepository implements BooksAuthorsRepository {
    public BooksAuthorsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Map<Long, List<Author>> mapAllByBook() {
        Map<Long, List<Author>> authorsMap = new LinkedHashMap<>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM book_authors;"
            );
        ) {
            while(result.next()) {
                Long book = result.getLong("book");
                Integer authorId = result.getInt("author");
                String name = result.getString("name");
                Author author = new Author(authorId, name);

                authorsMap.computeIfAbsent(
                    book,
                    v -> new ArrayList<Author>()
                ).add(author);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return authorsMap;
    };

    @Override
    public List<Author> findAllByBook(Long id) {
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
};