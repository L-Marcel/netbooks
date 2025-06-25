package app.netbooks.backend.transients;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import app.netbooks.backend.models.Tag;
import app.netbooks.backend.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {
    private String code;
    private User owner;
    private List<Participant> participants;
    private Map<Tag, Integer> bookChoices = new HashMap<>();

    public Room(String code, User owner) {
        this.code = code;
        this.owner = owner;
        this.participants = Collections.synchronizedList(
            new LinkedList<Participant>()
        );
    };
}
