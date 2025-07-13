package app.netbooks.backend.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.netbooks.backend.annotations.AdministratorOnly;
import app.netbooks.backend.annotations.AuthenticatedOnly;
import app.netbooks.backend.annotations.SubscriberOnly;
import app.netbooks.backend.authentication.AuthenticatedUser;
import app.netbooks.backend.dtos.request.LoginRequestBody;
import app.netbooks.backend.dtos.request.RegisterUserRequestBody;
import app.netbooks.backend.dtos.request.UpdateUserRequestBody;
import app.netbooks.backend.dtos.response.SubscriptionResponse;
import app.netbooks.backend.dtos.response.UserResponse;
import app.netbooks.backend.models.Role;
import app.netbooks.backend.models.User;
import app.netbooks.backend.services.RolesService;
import app.netbooks.backend.services.SubscriptionsService;
import app.netbooks.backend.services.TokensService;
import app.netbooks.backend.services.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private TokensService tokensService;

    @Autowired
    private SubscriptionsService subscriptionsService;

    @AdministratorOnly
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        List<User> users = this.usersService.findAll();
        Map<UUID, List<Role>> roles = this.rolesService.mapAllByUser();
        
        List<UserResponse> response = UserResponse.fromList(users, roles);
        return ResponseEntity.ok().body(response);
    };

    @AuthenticatedOnly
    @GetMapping("/me")
    public ResponseEntity<UserResponse> get(
        @AuthenticationPrincipal AuthenticatedUser user
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
        @RequestPart(name="avatar", required=false) MultipartFile avatar,
        @RequestPart("body") RegisterUserRequestBody body
    ) {
        usersService.register(
            body.getName(),
            avatar,
            body.getEmail(),
            body.getPassword(),
            body.getPasswordConfirmation()
        );
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    };

    @AuthenticatedOnly
    @PutMapping
    public ResponseEntity<UserResponse> update(
        @AuthenticationPrincipal AuthenticatedUser user,
        @RequestPart(name="avatar", required=false) MultipartFile avatar,
        @RequestPart("body") UpdateUserRequestBody body
    ) {
        usersService.update(
            user.getUser(),
            body.getName(),
            avatar,
            body.getEmail(),
            body.getPassword(),
            body.getPasswordConfirmation(),
            body.getOldPassword(),
            body.getUpdatePassword()
        );
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    };

    @SubscriberOnly
    @PostMapping("/switch-automatic-billing")
    public ResponseEntity<SubscriptionResponse> switchAutomaticBilling(
        @AuthenticationPrincipal AuthenticatedUser user
    ) {
        this.subscriptionsService.switchAutomaticBillingBySubscriber(
            user.getUser().getUuid()
        );
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    };
};