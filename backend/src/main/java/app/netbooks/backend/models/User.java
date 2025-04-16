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
    private String email;
    private String password;
    private String name;
    private int access;
};
