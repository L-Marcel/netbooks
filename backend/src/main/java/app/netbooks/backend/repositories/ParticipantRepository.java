package app.netbooks.backend.repositories;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.errors.ParticipantNotFound;
import app.netbooks.backend.models.User;
import app.netbooks.backend.transients.Participant;

@Repository
public class ParticipantRepository {
    private List<Participant> participants = Collections.synchronizedList(
        new LinkedList<Participant>()
    );

    public void add(Participant participant) {
        this.participants.add(participant);
    };

    public void remove(Participant participant) {
        this.participants.remove(participant);
    };

    private Participant find(Function<Participant, Boolean> search) throws ParticipantNotFound {
        synchronized(this.participants) {
            for(Participant participant : this.participants) {
                if(search.apply(participant))
                    return participant;
            };
            
            throw new ParticipantNotFound();
        }
    };
    
    public Participant findByUser(User user) {
        return this.find((participant) -> {
            return participant.getUser().getUuid().equals(user.getUuid());
        });
    };

    public Participant findByUuid(UUID uuid) {
        return this.find((participant) -> {
            return participant.getUser().getUuid().equals(uuid);
        });
    };
};
