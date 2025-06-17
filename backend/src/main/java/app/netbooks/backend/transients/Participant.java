package app.netbooks.backend.transients;

import app.netbooks.backend.models.User;
import lombok.Getter;

@Getter
public class Participant {
    private User user;
    private Room room;

    public Participant(User user) {
        this.user = user;
    }
    public Participant(User user, Room room) {
        this.user = user;
        this.room = room;
    }
}
