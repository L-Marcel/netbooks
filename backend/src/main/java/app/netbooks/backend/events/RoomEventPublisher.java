package app.netbooks.backend.events;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import app.netbooks.backend.dtos.response.BookResultResponse;
import app.netbooks.backend.dtos.response.RoomResponse;
import app.netbooks.backend.dtos.response.TagsOptionsResponse;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.models.Tag;
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

    public void emitOptionsToParticipants(Room room, List<Tag> tags) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/participants",
            TagsOptionsResponse.toTagsOptionsResponseList(tags)
        );
    };

    public void emitResultToParticipants(Room room, List<Book> books) {
        System.out.println("Envio de resultados disparado!");
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/participants/result",
            BookResultResponse.toBookResultResponseList(books)
        );
    };
    
}
