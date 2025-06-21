package app.netbooks.backend.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.dtos.response.ParticipantResponse;
import app.netbooks.backend.dtos.response.RoomResponse;
import app.netbooks.backend.models.User;
import app.netbooks.backend.services.ParticipantService;
import app.netbooks.backend.services.RoomService;
import app.netbooks.backend.transients.Participant;
import app.netbooks.backend.transients.Room;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private ParticipantService participantService;

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
        @AuthenticationPrincipal User user
    ) {
        Room createdRoom = roomService.createRoom(user);
        RoomResponse response = new RoomResponse(createdRoom);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    };

    @DeleteMapping
    public ResponseEntity<Void> closeRoom(
        @AuthenticationPrincipal User user
    ) {
        Room room = roomService.findRoomByUser(user);
        participantService.removeAllByRoom(room);
        roomService.closeRoom(room);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    };

    @GetMapping
    public ResponseEntity<RoomResponse> findRoomByUser(
        @AuthenticationPrincipal User user
    ) {
        Room room = roomService.findRoomByUser(user);
        RoomResponse response = new RoomResponse(room);

        return ResponseEntity.ok(response);
    };

    @GetMapping("/{code}")
    public ResponseEntity<RoomResponse> findRoomByCode(
        @PathVariable String code
    ) {
        Room room = roomService.findRoomByCode(code);
        RoomResponse response = new RoomResponse(room);

        return ResponseEntity.ok(response);
    };
    
    @PostMapping("/{code}/join")
    public ResponseEntity<ParticipantResponse> joinRoom(
        @PathVariable String code,
        @AuthenticationPrincipal User user
    ) {
        Room room = roomService.findRoomByCode(code);
        Participant participant = participantService.createParticipant(
            room,
            user
        );

        ParticipantResponse response = new ParticipantResponse(participant);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    };
};