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
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.repositories.interfaces.BooksBenefitsRepository;

@Repository
public class BooksBenefitsRepositoryImpl extends BaseRepository implements BooksBenefitsRepository {
    public BooksBenefitsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Map<Long, List<Benefit>> mapAllByBooks() {
        return this.queryOrDefault((connection) -> {
            Map<Long, List<Benefit>> benefitsMap = new LinkedHashMap<>();

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    // language=sql
                    """
                    SELECT * FROM book_benefit;
                    """
                );
            ) {
                while(result.next()) {
                    Long book = result.getLong("book");
                    String benefitName = result.getString("benefit");
                    Benefit benefit = new Benefit(benefitName);

                    benefitsMap.computeIfAbsent(
                        book,
                        v -> new ArrayList<Benefit>()
                    ).add(benefit);
                };
            };

            return benefitsMap;
        }, new LinkedHashMap<>());
    };

    @Override
    public List<Benefit> findAllByBook(Long id) {
        return this.queryOrDefault((connection) -> {
            List<Benefit> benefits = new LinkedList<>();

            try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT benefit FROM book_benefit WHERE book = ?;
                    """
                );
            ) {
                preparedStatement.setLong(1, id);
                
                try (ResultSet result = preparedStatement.executeQuery()) {
                    while(result.next()) {
                        String benefitName = result.getString("benefit");
                        
                        Benefit benefit = new Benefit(benefitName);

                        benefits.add(benefit);
                    };
                };
            };

            return benefits;
        }, new LinkedList<>());
    }

    @Override
    public void createMany(List<Benefit> benefits, Long book) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO book_benefit (benefit, book) VALUES (?, ?);
                    """
                );
            ) {
                for(Benefit benefit : benefits) {
                    statement.setString(1, benefit.getName());
                    statement.setLong(2, book);
                    statement.addBatch();
                };

                statement.executeBatch();
            };
        });
    }

    @Override
    public void deleteByBook(Long book) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    DELETE FROM book_benefit WHERE book = ?;
                    """
                );
            ) {
                statement.setLong(1, book);
                statement.executeUpdate();
            };
        });
    }

    @Override
    public Map<Long, List<Benefit>> mapAllByBooks(List<Book> books) {
        return this.queryOrDefault((connection) -> {
            Map<Long, List<Benefit>> benefitsMap = new LinkedHashMap<>();

            String ids = books.stream().map((book) -> book.getId().toString()).collect(Collectors.joining(", "));

            try (
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                    // language=sql
                    """
                    SELECT * FROM book_benefit
                    WHERE book IN
                    """
                    + " (" + ids + ");"
                );
            ) {
                while(result.next()) {
                    Long book = result.getLong("book");
                    String benefitName = result.getString("benefit");
                    Benefit benefit = new Benefit(benefitName);

                    benefitsMap.computeIfAbsent(
                        book,
                        v -> new ArrayList<Benefit>()
                    ).add(benefit);
                };
            };

            return benefitsMap;
        }, new LinkedHashMap<>());
    };
};
