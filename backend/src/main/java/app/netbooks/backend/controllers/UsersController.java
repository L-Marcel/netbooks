package app.netbooks.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.annotations.AdministratorOnly;
import app.netbooks.backend.annotations.AuhenticatedOnly;
import app.netbooks.backend.dtos.request.LoginRequestBody;
import app.netbooks.backend.dtos.request.RegisterUserRequestBody;
import app.netbooks.backend.dtos.request.UpdateUserRequestBody;
import app.netbooks.backend.dtos.response.UserResponse;
import app.netbooks.backend.models.User;
import app.netbooks.backend.services.TokensService;
import app.netbooks.backend.services.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private TokensService tokensService;

    @AdministratorOnly
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        List<User> users = this.usersService.findAll();
        List<UserResponse> response = UserResponse.fromList(users);
        return ResponseEntity.ok().body(response);
    };

    @AuhenticatedOnly
    @GetMapping("/me")
    public ResponseEntity<UserResponse> get(
        @AuthenticationPrincipal User user
    ) {
        UserResponse response = new UserResponse(user);
        return ResponseEntity.ok().body(response);
    };

    @PostMapping("/login")
    public ResponseEntity<String> login(
        @RequestBody LoginRequestBody body
    ) {
        User user = usersService.login(
            body.getEmail(), 
            body.getPassword()
        );

        String token = tokensService.generate(
            user.getUuid()
        );

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(token);
    };

    @PostMapping
    public ResponseEntity<Void> register(
        @RequestBody RegisterUserRequestBody body
    ) {
        usersService.register(
            body.getName(),
            body.getAvatar(),
            body.getEmail(),
            body.getPassword(),
            body.getPasswordConfirmation()
        );
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    };

    @AuhenticatedOnly
    @PutMapping
    public ResponseEntity<UserResponse> update(
        @AuthenticationPrincipal User user,
        @RequestBody UpdateUserRequestBody body
    ) {
        usersService.update(
            user,
            body.getName(),
            body.getAvatar(),
            body.getEmail(),
            body.getPassword()
        );
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    };
};