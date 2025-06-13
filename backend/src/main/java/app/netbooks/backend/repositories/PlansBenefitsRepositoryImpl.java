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
import app.netbooks.backend.repositories.interfaces.PlansBenefitsRepository;

@Repository
public class PlansBenefitsRepositoryImpl extends BaseRepository implements PlansBenefitsRepository {
    public PlansBenefitsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Map<Integer, List<Benefit>> mapAllAvailableByPlan() {
        Map<Integer, List<Benefit>> benefitsMap = new LinkedHashMap<>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM plan_benefits WHERE available;"
            );
        ) {
            while(result.next()) {
                Integer plan = result.getInt("plan");
                String benefitName = result.getString("benefit");
                Benefit benefit = new Benefit(benefitName);

                benefitsMap.computeIfAbsent(
                    plan,
                    v -> new ArrayList<Benefit>()
                ).add(benefit);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return benefitsMap;
    };

    @Override
    public Map<Integer, List<Benefit>> mapAllByPlan() {
        Map<Integer, List<Benefit>> benefitsMap = new LinkedHashMap<>();

        try (
            Connection connection = this.database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                "SELECT * FROM plan_benefit;"
            );
        ) {
            while(result.next()) {
                Integer plan = result.getInt("plan");
                String benefitName = result.getString("benefit");
                Benefit benefit = new Benefit(benefitName);

                benefitsMap.computeIfAbsent(
                    plan,
                    v -> new ArrayList<Benefit>()
                ).add(benefit);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        };

        return benefitsMap;
    };

    @Override
    public List<Benefit> findAllByPlan(Integer plan) {
        List<Benefit> benefits = new LinkedList<>();

        try (
            Connection connection = this.database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT benefit FROM plan_benefit WHERE plan = ?;"
            );
        ) {
            preparedStatement.setInt(1, plan);
            
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
