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
                    // language=sql
                    """
                    SELECT * FROM subscription_with_state
                    WHERE actived AND subscriber = ? LIMIT 1;
                    """
                );
            ) {
                statement.setString(1, subscriber.toString());

                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        Long id = result.getLong("id");
                        Integer edition = result.getInt("edition");
                        Date startedIn = result.getDate("started_in");
                        Date closedIn = result.getDate("closed_in");
                        Boolean actived = result.getBoolean("actived");
                        
                        Subscription user = new Subscription(
                            id,
                            subscriber,
                            edition,
                            startedIn,
                            closedIn,
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
    public Optional<Subscription> findNextScheduledBySubscriber(
        UUID subscriber
    ) {
        return this.queryOrDefault((connection) -> {
            Optional<Subscription> subscriptionFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM subscription_with_state
                    WHERE subscriber = ?
                    AND NOT actived AND started_in > CURRENT_DATE
                    AND closed_in IS NULL
                    ORDER BY started_in ASC
                    LIMIT 1;
                    """
                );
            ) {
                statement.setString(1, subscriber.toString());

                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        Long id = result.getLong("id");
                        Integer edition = result.getInt("edition");
                        Date startedIn = result.getDate("started_in");
                        Date closedIn = result.getDate("closed_in");
                        Boolean actived = result.getBoolean("actived");
                        
                        Subscription user = new Subscription(
                            id,
                            subscriber,
                            edition,
                            startedIn,
                            closedIn,
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
    public Optional<Subscription> findById(
        Long id
    ) {
        return this.queryOrDefault((connection) -> {
            Optional<Subscription> subscriptionFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM subscription_with_state
                    WHERE actived AND id = ?;
                    """
                );
            ) {
                statement.setLong(1, id);

                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        UUID subscriber = UUID.fromString(result.getString("subscriber"));
                        Integer edition = result.getInt("edition");
                        Date startedIn = result.getDate("started_in");
                        Date closedIn = result.getDate("closed_in");
                        Boolean actived = result.getBoolean("actived");
                        
                        Subscription user = new Subscription(
                            id,
                            subscriber,
                            edition,
                            startedIn,
                            closedIn,
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
    public void subscribe(Subscription subscription) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT IGNORE INTO subscriber (uuid) VALUES (?);
                    """
                );
            ) {
                statement.setString(1, subscription.getSubscriber().toString());
                statement.executeUpdate();
            }; 

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO subscription (edition, subscriber, started_in) 
                    VALUES (?, ?, ?);        
                    """,
                    true
                );
            ) {
                statement.setInt(1, subscription.getEdition());
                statement.setString(2, subscription.getSubscriber().toString());
                statement.setDate(3, subscription.getStartedIn());
                statement.executeUpdate();
                
                try (ResultSet result = statement.getGeneratedKeys()) {
                    result.next();
                    Long id = result.getLong(1);
                    subscription.setId(id);
                };
            };
        });
    };

    @Override
    public void closeById(Long id) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE subscription
                    SET closed_in = CURRENT_DATE
                    WHERE id = ?;
                    """
                );
            ) {
                statement.setLong(1, id);
                statement.executeUpdate();
            };
        });
    };

    @Override
    public void reopenById(Long id) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE subscription
                    SET closed_in = NULL
                    WHERE id = ?;
                    """
                );
            ) {
                statement.setLong(1, id);
                statement.executeUpdate();
            };
        });
    };

    @Override
    public void closeById(Long id, Date closeDate) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE subscription
                    SET closed_in = ?
                    WHERE id = ?;
                    """
                );
            ) {
                statement.setDate(1, closeDate);
                statement.setLong(2, id);
                statement.executeUpdate();
            };
        });
    };

    @Override
    public void closeNotClosedBySubscriber(UUID subscriber) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE subscription  
                    SET closed_in = COALESCE(closed_in, CURRENT_DATE)
                    WHERE subscriber = ?;
                    """
                );
            ) {
                statement.setString(1, subscriber.toString());
                statement.executeUpdate();
            };
        });
    };

    @Override
    public void deleteScheduledsBySubscriber(UUID subscriber) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    DELETE FROM subscription  
                    WHERE subscriber = ?
                    AND started_in > CURRENT_DATE
                    AND closed_in IS NULL;
                    """
                );
            ) {
                statement.setString(1, subscriber.toString());
                statement.executeUpdate();
            };
        });
    };
};
