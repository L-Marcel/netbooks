package app.netbooks.backend.dtos.response;

import java.util.List;
import java.util.stream.Collectors;

import app.netbooks.backend.models.User;
import app.netbooks.backend.transients.Room;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomResponse {
    private String code;
    private User owner;
    private List<ParticipantResponse> participants;

    public RoomResponse(Room room) {
        this.code = room.getCode();
        this.owner = room.getOwner();
        this.participants = room.getParticipants()
            .stream()
            .map(ParticipantResponse::new)
            .collect(Collectors.toList());
    };
};
