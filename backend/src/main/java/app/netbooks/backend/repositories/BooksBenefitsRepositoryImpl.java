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
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.repositories.interfaces.BooksBenefitsRepository;

@Repository
public class BooksBenefitsRepositoryImpl extends BaseRepository implements BooksBenefitsRepository {
    public BooksBenefitsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Map<Long, List<Benefit>> mapAllByBook() {
        Map<Long, List<Benefit>> benefitsMap = new LinkedHashMap<>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM book_benefit;"
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
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return benefitsMap;
    };

    @Override
    public List<Benefit> findAllByBook(Long id) {
        List<Benefit> benefits = new LinkedList<>();

        try (
            Connection connection = this.database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT benefit FROM book_benefit WHERE book = ?;"
            );
        ) {
            preparedStatement.setLong(1, id);
            
            try (
                ResultSet result = preparedStatement.executeQuery();
            ) {
                while(result.next()) {
                    String benefitName = result.getString("benefit");
                    
                    Benefit benefit = new Benefit(benefitName);

                    benefits.add(benefit);
                };
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return benefits;
    };
};
