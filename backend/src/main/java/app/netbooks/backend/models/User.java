package app.netbooks.backend.models;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    private List<Role> roles;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    };

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
            .filter((Role role) -> role != Role.UNKNOWN)
            .map((Role role) -> new SimpleGrantedAuthority("ROLE_" + role.toString()))
            .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.uuid.toString();
    };
};
