package app.netbooks.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Person {
    private String email;
    private String password;
    private String name;
    private int access;
};
