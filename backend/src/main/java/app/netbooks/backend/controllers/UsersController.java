package app.netbooks.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.annotations.AdministratorOnly;
import app.netbooks.backend.dtos.LoginRequestBody;
import app.netbooks.backend.dtos.RegisterRequestBody;
import app.netbooks.backend.dtos.UserResponse;
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

    @AdministratorOnly
    @GetMapping("/validate")
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
            .status(HttpStatus.ACCEPTED)
            .body(token);
    };

    @PostMapping
    public ResponseEntity<String> register(
        @RequestBody RegisterRequestBody body
    ) {
        usersService.register(
            body.getName(),
            body.getEmail(),
            body.getPassword()
        );
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("Usuário registrado com sucesso!");
    };
};