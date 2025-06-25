package app.netbooks.backend.authentication;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import app.netbooks.backend.models.Role;
import app.netbooks.backend.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticatedUser implements UserDetails {
    private User user;
    private List<Role> roles;

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles().stream()
            .filter((Role role) -> role != Role.UNKNOWN)
            .map((Role role) -> new SimpleGrantedAuthority("ROLE_" + role.toString()))
            .collect(Collectors.toList());
    };

    @Override
    public String getUsername() {
        return this.getUser().getEmail();
    };

    @Override
    public String getPassword() {
        return this.getUser().getPassword();
    };
};
