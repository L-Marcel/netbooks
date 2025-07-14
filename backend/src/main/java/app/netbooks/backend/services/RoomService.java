package app.netbooks.backend.services;

import java.security.SecureRandom;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.errors.UserRoomAlreadyExists;
import app.netbooks.backend.events.RoomEventPublisher;
import app.netbooks.backend.errors.ParticipantAlreadyInRoom;
import app.netbooks.backend.errors.RoomNotFound;
import app.netbooks.backend.models.Tag;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.RoomRepository;
import app.netbooks.backend.transients.Room;

@Service
public class RoomService {
    @Autowired
    private RoomRepository repository;

    @Autowired
    private RoomEventPublisher roomEventPublisher;

    private String generateCode(int size) {
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        
        for(int i = 0; i < size; i++) {
            builder.append(random.nextInt(10));
        };

        return builder.toString();
    };

    public Room findRoomByCode(
        String code
    ) throws RoomNotFound {
        return repository.findByCode(code)
            .orElseThrow(RoomNotFound::new);
    };

    public Room findRoomByUser(
        User user
    ) throws RoomNotFound {
        return repository.findByUser(user)
            .orElseThrow(RoomNotFound::new);
    };

    public Room createRoom(
        User user
    ) throws UserRoomAlreadyExists {
        synchronized(repository) {
            if(repository.existsByUser(user)) 
            throw new UserRoomAlreadyExists();

            String code = this.generateCode(6);
            while(repository.existsByCode(code)) 
                code = code.concat(this.generateCode(2));
            
            Room room = new Room(code, user);

            repository.add(room);
            return room;
        }
    };

    public void closeRoom(
        Room room
    ) throws RoomNotFound, ParticipantAlreadyInRoom {
        repository.remove(room);
        roomEventPublisher.emitRoomClosed(room);
    };

    public void sendOptions(
        Room room,
        List<Tag> tags
    ) {
        roomEventPublisher.emitOptionsToParticipants(room, tags);
    }
};
