package app.netbooks.backend.dtos.response;

import java.util.UUID;

import app.netbooks.backend.transients.Participant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantResponse {
    private UUID user;
    private String name;
    private String room;

    public ParticipantResponse(Participant participant) {
        this.user = participant.getUser().getUuid();
        this.name = participant.getUser().getName();
        this.room = participant.getRoom().getCode();
    };
};
