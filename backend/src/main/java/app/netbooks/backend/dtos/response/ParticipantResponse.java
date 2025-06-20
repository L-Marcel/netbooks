package app.netbooks.backend.dtos.response;

import java.util.UUID;

import app.netbooks.backend.transients.Participant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantResponse {
    private UUID user;
    private String room;

    public ParticipantResponse(Participant participant) {
        this.room = participant.getRoom().getCode();
        this.user = participant.getUser().getUuid();
    };
};
