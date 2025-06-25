package app.netbooks.backend.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import app.netbooks.backend.dtos.response.RoomResponse;
import app.netbooks.backend.transients.Room;

@Component
public class RoomEventPublisher {
    @Autowired
    private SimpMessagingTemplate simp;

    public void emitRoomUpdated(Room room) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/updated", 
            new RoomResponse(room)
        );
    };

    public void emitRoomClosed(Room room) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/closed",
            "Sala fechada!"
        );
    };
    
}
