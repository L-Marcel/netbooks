package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class ParticipantAlreadyInRoom extends HttpError {
    public ParticipantAlreadyInRoom() {
        super(
            "Conflito de participantes, tente usar outra conta, " + 
            "se não der certo, verifique se já não está na sala.", 
            HttpStatus.CONFLICT
        );
    };
};
