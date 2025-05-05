package app.netbooks.backend.models;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User implements UserDetails {
    private UUID uuid;
    private String name;
    private String email;
    private String password;
    private Access access;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.access = Access.DEFAULT;
    };

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.access.toRole()));
    }

    @Override
    public String getUsername() {
        return this.uuid.toString();
    };
};
