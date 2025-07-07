package app.netbooks.backend.repositories;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Reading;
import app.netbooks.backend.repositories.interfaces.ReadingsRepository;

@Repository
public class ReadingsRepositoryImpl extends BaseRepository implements ReadingsRepository {
    public ReadingsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Optional<Reading> findByUserAndId(UUID user, Long id) {
        return this.queryOrDefault((connection) -> {
            Optional<Reading> readingFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM reading_with_percentage 
                    WHERE id = ?
                    AND user = ?;
                    """
                );
            ) {
                statement.setLong(1, id);
                statement.setString(2, user.toString());
                try(ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        Date startedIn = result.getDate("started_in");
                        Date stoppedIn = result.getDate("stopped_in");
                        Boolean finished = result.getBoolean("finished");
                        Integer currentPage = result.getInt("current_page");
                        Integer numPages = result.getInt("num_pages");
                        Double percentage = result.getDouble("percentage");
                        Long book = result.getLong("book");
                        
                        Reading reading = new Reading(
                            id,
                            startedIn,
                            stoppedIn,
                            finished,
                            currentPage,
                            numPages,
                            percentage,
                            user,
                            book
                        );

                        readingFound = Optional.of(reading);
                    };
                };
            };

            return readingFound;
        }, Optional.empty());
    };

    @Override
    public List<Reading> findAllByUser(UUID user) {
        return this.queryOrDefault((connection) -> {
            List<Reading> readings = new ArrayList<Reading>();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM reading_with_percentage
                    WHERE user = ?
                    ORDER BY finished ASC, started_in DESC, id DESC;  
                    """
                );
            ) {
                statement.setString(1, user.toString());
                try(ResultSet result = statement.executeQuery()) {
                    while(result.next()) {
                        Long id = result.getLong("id");
                        Date startedIn = result.getDate("started_in");
                        Date stoppedIn = result.getDate("stopped_in");
                        Boolean finished = result.getBoolean("finished");
                        Integer currentPage = result.getInt("current_page");
                        Integer numPages = result.getInt("num_pages");
                        Double percentage = result.getDouble("percentage");
                        Long book = result.getLong("book");
                        
                        Reading reading = new Reading(
                            id,
                            startedIn,
                            stoppedIn,
                            finished,
                            currentPage,
                            numPages,
                            percentage,
                            user,
                            book
                        );

                        readings.add(reading);
                    };
                };
            };

            return readings;
        }, new ArrayList<>());
    };

    @Override
    public List<Reading> findAllByUserAndBook(UUID user, Long book) {
        return this.queryOrDefault((connection) -> {
            List<Reading> readings = new ArrayList<Reading>();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM reading_with_percentage
                    WHERE user = ?
                    AND book = ?
                    ORDER BY finished ASC, started_in DESC, id DESC;  
                    """
                );
            ) {
                statement.setString(1, user.toString());
                statement.setLong(2, book);
                try(ResultSet result = statement.executeQuery()) {
                    while(result.next()) {
                        Long id = result.getLong("id");
                        Date startedIn = result.getDate("started_in");
                        Date stoppedIn = result.getDate("stopped_in");
                        Boolean finished = result.getBoolean("finished");
                        Integer currentPage = result.getInt("current_page");
                        Integer numPages = result.getInt("num_pages");
                        Double percentage = result.getDouble("percentage");
                        
                        Reading reading = new Reading(
                            id,
                            startedIn,
                            stoppedIn,
                            finished,
                            currentPage,
                            numPages,
                            percentage,
                            user,
                            book
                        );

                        readings.add(reading);
                    };
                };
            };

            return readings;
        }, new ArrayList<>());
    };

    @Override
    public void create(Reading reading) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO reading (user, book) VALUES (?, ?);
                    """,
                    true
                );
            ) {
                statement.setString(1, reading.getUser().toString());
                statement.setLong(2, reading.getBook());
                statement.executeUpdate();

                try (ResultSet result = statement.getGeneratedKeys()) {
                    result.next();
                    Long id = result.getLong(1);
                    reading.setId(id);
                };
            };
        });
    };

    @Override
    public void updatePageById(Long id, Integer page) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE reading
                    SET current_page = ?
                    WHERE id = ?;
                    """
                );
            ) {
                statement.setInt(1, page);
                statement.setLong(2, id);
                statement.executeUpdate();
            };
        });
    };

    @Override
    public void finishById(Long id) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE reading
                    SET finished = TRUE
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
    public void stopById(Long id) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE reading
                    SET stopped_in = CURRENT_DATE
                    WHERE id = ?;
                    """
                );
            ) {
                statement.setLong(1, id);
                statement.executeUpdate();
            };
        });
    }

    @Override
    public Integer countNotFinishedReadingsByUserAndBook(UUID user, Long book) {
        return this.queryOrDefault((connection) -> {
            Integer count = 0;

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT COUNT(*) AS count FROM reading
                    WHERE NOT finished 
                    AND user = ?
                    AND book = ?;
                    """
                );
            ) {
                statement.setString(1, user.toString());
                statement.setLong(2, book);
                try(ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        count = result.getInt("count");
                    };
                };
            };

            return count;
        }, 0);
    }

    @Override
    public Optional<Reading> findLastByUserAndBook(UUID user, Long book) {
        return this.queryOrDefault((connection) -> {
            Optional<Reading> readingFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM reading_with_percentage
                    WHERE user = ?
                    AND book = ?
                    ORDER BY finished ASC, started_in DESC, id DESC;
                    """
                );
            ) {
                statement.setString(1, user.toString());
                statement.setLong(2, book);
                try(ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        Long id = result.getLong("id");
                        Date startedIn = result.getDate("started_in");
                        Date stoppedIn = result.getDate("stopped_in");
                        Boolean finished = result.getBoolean("finished");
                        Integer currentPage = result.getInt("current_page");
                        Integer numPages = result.getInt("num_pages");
                        Double percentage = result.getDouble("percentage");
                        
                        Reading reading = new Reading(
                            id,
                            startedIn,
                            stoppedIn,
                            finished,
                            currentPage,
                            numPages,
                            percentage,
                            user,
                            book
                        );

                        readingFound = Optional.of(reading);
                    };
                };
            };

            return readingFound;
        }, Optional.empty());
    };
};
