package app.netbooks.backend.repositories;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Subscription;
import app.netbooks.backend.repositories.interfaces.SubscriptionsRepository;

@Repository
public class SubscriptionsRepositoryImpl extends BaseRepository implements SubscriptionsRepository {
    public SubscriptionsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Optional<Subscription> findBySubscriber(
        UUID subscriber
    ) {
        return this.queryOrDefault((connection) -> {
            Optional<Subscription> subscriptionFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM subscription_with_state \n" +
                    "WHERE actived AND subscriber = ? LIMIT 1;"
                );
            ) {
                statement.setString(1, subscriber.toString());

                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        Long id = result.getLong("id");
                        Integer edition = result.getInt("edition");
                        Date startedIn = result.getDate("started_in");
                        Date closedIn = result.getDate("closed_in");
                        Boolean automaticBilling = result.getBoolean("automatic_billing");
                        Boolean actived = result.getBoolean("actived");
                        
                        Subscription user = new Subscription(
                            id,
                            subscriber,
                            edition,
                            startedIn,
                            closedIn,
                            automaticBilling,
                            actived
                        );

                        subscriptionFound = Optional.of(user);
                    };
                };
            };

            return subscriptionFound;
        }, Optional.empty());
    };

    @Override
    public Long subscribe(
        UUID subscriber,
        Integer edition
    ) {
        return this.query((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO subscription (edition, subscriber) VALUES (?, ?);",
                    true
                );
            ) {
                statement.setInt(1, edition);
                statement.setString(2, subscriber.toString());
                
                try (
                    ResultSet result = statement.getGeneratedKeys()
                ) {
                    result.next();
                    return result.getLong(1);
                }
            }
        });
    };

    @Override
    public void createSubscriberIfNotExists(
        UUID subscriber
    ) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT IGNORE INTO subscriber (uuid) VALUES (?);"
                );
            ) {
                statement.setString(1, subscriber.toString());
                statement.executeUpdate();
            }; 
        });
    };

    @Override
    public void closeOldSubscriptions(
        UUID subscriber
    ) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    "UPDATE subscription\n" +
                    "SET closed_in = COALESCE(closed_in, CURRENT_DATE), \n" +
                    "automatic_billing = 0\n" +
                    "WHERE subscriber = ?;"
                );
            ) {
                statement.setString(1, subscriber.toString());
                statement.executeUpdate();
            };

            try (
                PreparedStatement statement = connection.prepareStatement(
                    "UPDATE payment AS pay\n" +
                    "JOIN subscription AS sub\n" +
                    "SET pay.status = 'CANCELED'\n" +
                    "WHERE pay.status = 'SCHEDULED'\n" +
                    "AND pay.subscription = sub.id\n" +
                    "AND sub.subscriber = ?;"
                );
            ) {
                statement.setString(1, subscriber.toString());
                statement.executeUpdate();
            };
        });
    };

    @Override
    public void upgrade( 
        UUID subscriber, 
        Long subscription, 
        Integer newEdition
    ) {
        this.execute((connection) -> {
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
        });
    };

    @Override
    public void downgrade(
        UUID subscriber, 
        Long subscription,
        Integer newEdition
    ) {
        this.execute((connection) -> {
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
        });
    };

    @Override
    public void unsubscribe(UUID subscriber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unsubscribe'");
    };
};
