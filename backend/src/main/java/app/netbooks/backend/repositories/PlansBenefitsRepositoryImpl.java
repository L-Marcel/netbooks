package app.netbooks.backend.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.repositories.interfaces.PlansBenefitsRepository;

@Repository
public class PlansBenefitsRepositoryImpl extends BaseRepository implements PlansBenefitsRepository {
    public PlansBenefitsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Map<Integer, List<Benefit>> mapAllAvailableByPlan() {
        return this.queryOrDefault((connection) -> {
            Map<Integer, List<Benefit>> benefitsMap = new LinkedHashMap<>();

            try (
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
            };

            return benefitsMap;
        }, new LinkedHashMap<>());
    };

    @Override
    public Map<Integer, List<Benefit>> mapAllByPlan() {
        return this.queryOrDefault((connection) -> {
            Map<Integer, List<Benefit>> benefitsMap = new LinkedHashMap<>();

            try (
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
            };

            return benefitsMap;
        }, new LinkedHashMap<>());
    };

    @Override
    public List<Benefit> findAllByPlan(Integer plan) {
        return this.queryOrDefault((connection) -> {
            List<Benefit> benefits = new LinkedList<>();

            try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT benefit FROM plan_benefit WHERE plan = ?;"
                );
            ) {
                preparedStatement.setInt(1, plan);
                
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
    };
};
