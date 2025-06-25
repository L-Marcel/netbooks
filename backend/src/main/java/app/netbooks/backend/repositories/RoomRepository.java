package app.netbooks.backend.repositories;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.models.User;
import app.netbooks.backend.transients.Room;

@Repository
public class RoomRepository {
    private List<Room> rooms = Collections.synchronizedList(
        new LinkedList<Room>()
    );

    public void add(Room room) {
        this.rooms.add(room);
    };

    public void remove(Room room) {
        this.rooms.remove(room);
    };

    private Optional<Room> find(Function<Room, Boolean> search) {
        synchronized(this.rooms) {
            for(Room room : this.rooms) {
                if(search.apply(room))
                    return Optional.of(room);
            };
    
            return Optional.empty();
        }
    };

    public Optional<Room> findByUser(User user) {
        return this.find((room) -> {
            return room.getOwner()
                .getUuid()
                .equals(user.getUuid());
        });
    };

    public Optional<Room> findByCode(String code) {
        return this.find((room) -> {
            return room.getCode().equals(code);
        });
    };

    public boolean existsByCode(String code) {
        return this.findByCode(code).isPresent();
    };

    public boolean existsByUser(User user) {
        return this.findByUser(user).isPresent();
    };
};
