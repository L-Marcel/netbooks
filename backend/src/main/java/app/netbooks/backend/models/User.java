package app.netbooks.backend.models;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private UUID uuid;
    private String name;
    private String email;
    private String password;
    private Boolean automaticBilling;

    public User(String name, String email, String password) {
        this(UUID.randomUUID(), name, email, password, true);
    };
};
