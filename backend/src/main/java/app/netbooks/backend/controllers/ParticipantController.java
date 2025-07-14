package app.netbooks.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.annotations.SubscriberOnly;
import app.netbooks.backend.annotations.SubscriberOrAdministratorOnly;
import app.netbooks.backend.authentication.AuthenticatedUser;
import app.netbooks.backend.dtos.response.ParticipantResponse;
import app.netbooks.backend.services.ParticipantService;
import app.netbooks.backend.transients.Participant;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    @Autowired
    private ParticipantService participantService;

    @SubscriberOrAdministratorOnly
    @GetMapping("/me")
    public ResponseEntity<ParticipantResponse> findParticipantByUser(
        @AuthenticationPrincipal AuthenticatedUser user
    ) {
        Participant participant = participantService.findParticipantByUser(user.getUser());
        ParticipantResponse response = new ParticipantResponse(participant);

        return ResponseEntity.ok(response);
    };

    @SubscriberOrAdministratorOnly
    @DeleteMapping
    public ResponseEntity<Void> kickFromRoom(
        @AuthenticationPrincipal AuthenticatedUser user
    ) {
        Participant participant = participantService.findParticipantByUser(user.getUser());
        participantService.removeParticipant(participant);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    };
};
