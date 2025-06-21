package app.netbooks.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.dtos.response.ParticipantResponse;
import app.netbooks.backend.models.User;
import app.netbooks.backend.services.ParticipantService;
import app.netbooks.backend.transients.Participant;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    @Autowired
    private ParticipantService participantService;

    @GetMapping("/me")
    public ResponseEntity<ParticipantResponse> findParticipantByUser(
        @AuthenticationPrincipal User user
    ) {
        Participant participant = participantService.findParticipantByUser(user);
        ParticipantResponse response = new ParticipantResponse(participant);

        return ResponseEntity.ok(response);
    };

    @DeleteMapping
    public ResponseEntity<Void> kickFromRoom(
        @AuthenticationPrincipal User user
    ) {
        Participant participant = participantService.findParticipantByUser(user);
        participantService.removeParticipant(participant);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    };
};
