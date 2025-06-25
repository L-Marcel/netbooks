package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.Database;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.models.Subscription;
import app.netbooks.backend.repositories.interfaces.SubscriptionsRepository;

@Repository
public class SubscriptionsRepositoryImpl extends BaseRepository implements SubscriptionsRepository {
    public SubscriptionsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Optional<Subscription> findBySubscriber(UUID subscriber) {
        try (
            Connection connection = this.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM subscription_with_state \n" +
                "WHERE actived AND subscriber = ? LIMIT 1;"
            );
        ) {
            statement.setString(1, subscriber.toString());

            try (ResultSet result = statement.executeQuery();) {
                Optional<Subscription> subscriptionFound = Optional.empty();

                if(result.next()) {
                    Long id = result.getLong("id");
                    Integer edition = result.getInt("edition");
                    Date startedIn = result.getDate("started_in");
                    Date closedIn = result.getDate("closed_in");
                    Date dueDate = result.getDate("due_date");
                    Date payDate = result.getDate("pay_date");
                    Integer numPayments = result.getInt("num_payments");
                    Boolean automaticBilling = result.getBoolean("automatic_billing");
                    Boolean actived = result.getBoolean("actived");
                    
                    Subscription user = new Subscription(
                        id,
                        subscriber,
                        edition,
                        startedIn,
                        closedIn,
                        dueDate,
                        payDate,
                        numPayments,
                        automaticBilling,
                        actived
                    );

                    subscriptionFound = Optional.of(user);
                };

                return subscriptionFound;
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    };

    @Override
    public void subscribe(UUID subscriber, Integer edition) {
        try (
            Connection connection = this.database.getConnection();
        ) {
            connection.setAutoCommit(false);

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT IGNORE INTO subscriber (uuid) VALUES (?);"
                );
            ) {
                statement.setString(1, subscriber.toString());
                statement.executeUpdate();
            };

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "UPDATE subscription\n" +
                    "SET closed_in = CURRENT_DATE,\n" +
                    "automatic_billing = 0\n" +
                    "WHERE subscriber = ?;"
                );
            ) {
                statement.setString(1, subscriber.toString());
                statement.executeUpdate();
            };

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO subscription (edition, subscriber) VALUES \n" +
                    "  (?, ?);"
                );
            ) {
                statement.setInt(1, edition);
                statement.setString(2, subscriber.toString());
                statement.executeUpdate();
            };
            
            connection.commit();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    };

    @Override
    public void unsubscribe(UUID subscriber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unsubscribe'");
    };
};
