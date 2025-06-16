package app.netbooks.backend.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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

            Long previous = null;

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "SELECT id FROM subscription_with_state\n" +
                    "WHERE (NOT actived) AND subscriber = ?\n" +
                    "ORDER BY started_in DESC, (closed_in IS NULL) DESC\n" +
                    "LIMIT 1;"
                );
            ) {
                statement.setString(1, subscriber.toString());
            
                try (
                    ResultSet result = statement.executeQuery();
                ) {
                    if(result.next()) {
                        previous = result.getLong("id");
                    };
                };
            };

            if(previous != null) {
                try (
                    PreparedStatement statement = connection.prepareStatement(
                        "UPDATE subscription\n" +
                        "SET closed_in = COALESCE(closed_in, CURRENT_DATE), \n" +
                        "automatic_billing = 0\n" +
                        "WHERE id = ?;"
                    );
                ) {
                    statement.setLong(1, previous);
                    statement.executeUpdate();
                };
            };

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO subscription (edition, subscriber, previous) VALUES \n" +
                    "  (?, ?, ?);"
                );
            ) {
                statement.setInt(1, edition);
                statement.setString(2, subscriber.toString());
                if(previous != null) statement.setLong(3, previous);
                else statement.setNull(3, Types.BIGINT);
                statement.executeUpdate();
            };
            
            connection.commit();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    };

    @Override
    public void unsubscribe(UUID subscriber) {
        throw new UnsupportedOperationException("Unimplemented method 'unsubscribe'");
    }

    @Override
    public void upgrade(UUID subscriber, Long subscription, Integer newEdition) {
        try (
            Connection connection = this.database.getConnection();
        ) {
            connection.setAutoCommit(false);

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "UPDATE subscription\n" +
                    "SET closed_in = CURRENT_DATE, \n" +
                    "automatic_billing = 0\n" +
                    "WHERE id = ?;"
                );
            ) {
                statement.setLong(1, subscription);
                statement.executeUpdate();
            };

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO subscription (edition, subscriber, previous) VALUES \n" +
                    "  (?, ?, ?);"
                );
            ) {
                statement.setInt(1, newEdition);
                statement.setString(2, subscriber.toString());
                statement.setLong(3, subscription);
                statement.executeUpdate();
            };

            connection.commit();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }

    @Override
    public void downgrade(UUID subscriber, Long subscription, Integer newEdition) {
        try (
            Connection connection = this.database.getConnection();
        ) {
            connection.setAutoCommit(false);

            Date dueDate = null;

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "CALL get_due_date(?);"
                );
            ) {
                statement.setLong(1, subscription);
            
                try (
                    ResultSet result = statement.executeQuery();
                ) {
                    result.next();
                    dueDate = result.getDate("due_date");
                };
            };

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "UPDATE subscription\n" +
                    "SET closed_in = ?, \n" +
                    "automatic_billing = 0\n" +
                    "WHERE id = ?;"
                );
            ) {
                statement.setDate(1, dueDate);
                statement.setLong(2, subscription);
                statement.executeUpdate();
            };

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO subscription (edition, subscriber, started_in, num_payments, previous) VALUES \n" +
                    "  (?, ?, ?, ?, ?);"
                );
            ) {
                statement.setInt(1, newEdition);
                statement.setString(2, subscriber.toString());
                statement.setDate(3, dueDate);
                statement.setInt(4, 0);
                statement.setLong(5, subscription);
                statement.executeUpdate();
            };

            connection.commit();
        } catch (SQLException e) {
            throw new InternalServerError();
        }
    };
};
