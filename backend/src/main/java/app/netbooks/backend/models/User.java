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
    private String avatar;
    private String email;
    private String password;

    public User(String name, String avatar, String email, String password) {
        this(UUID.randomUUID(), name, avatar, email, password);
    };
};
