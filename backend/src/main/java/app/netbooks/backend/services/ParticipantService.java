package app.netbooks.backend.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.errors.ParticipantAlreadyInRoom;
import app.netbooks.backend.errors.ParticipantNotFound;
import app.netbooks.backend.events.RoomEventPublisher;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.ParticipantRepository;
import app.netbooks.backend.transients.Participant;
import app.netbooks.backend.transients.Room;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository repository;

    @Autowired
    private RoomEventPublisher roomEventPublisher;

    private boolean isParticipantAlreadyInRoom(
        Room room, 
        Participant candidate
    ) {
        for(Participant participant : room.getParticipants()) {
            boolean userAlreadyInUse = false;
            User user = participant.getUser();
            User candidateUser = candidate.getUser();
            
            userAlreadyInUse = user.getEmail().equals(
                candidateUser.getEmail()
            );

            if(userAlreadyInUse) return true;
        };

        return false;
    };
    
    public Participant createParticipant(
        Room room, 
        User user
    ) throws ParticipantAlreadyInRoom,ParticipantNotFound {
        Participant participant;

        if(user != null) participant = new Participant(user, room);
        else throw new ParticipantNotFound();

        synchronized(room.getParticipants()) {
            boolean alreadyInRoom = this.isParticipantAlreadyInRoom(room, participant);
            if(alreadyInRoom) throw new ParticipantAlreadyInRoom();

            this.repository.add(participant);
            room.getParticipants().add(participant);
        }

        this.roomEventPublisher.emitRoomUpdated(participant.getRoom());

        return participant;
    };

    public Participant findParticipantByUser(
        User user
    ) {
        return this.repository.findByUser(user);
    };

    public Participant findParticipantByUuid(
        UUID uuid
    ) {
        return this.repository.findByUuid(uuid);
    };

    public void removeAllByRoom(Room room) {
        synchronized(room.getParticipants()) {
            for (Participant participant : room.getParticipants()) {
                this.repository.remove(participant);
            };
        };
    };

    public void removeParticipant(Participant participant) {
        synchronized(participant.getRoom().getParticipants()) {
            participant.getRoom().getParticipants().remove(participant);
            this.repository.remove(participant);
        };

        this.roomEventPublisher.emitRoomUpdated(participant.getRoom());
    };
};
