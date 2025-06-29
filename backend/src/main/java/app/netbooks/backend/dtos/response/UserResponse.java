package app.netbooks.backend.dtos.response;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import app.netbooks.backend.authentication.AuthenticatedUser;

import app.netbooks.backend.models.Role;
import app.netbooks.backend.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID uuid;
    private String name;
    private String email;
    private Boolean automaticBilling;
    private List<Role> roles;

    public UserResponse(AuthenticatedUser user) {
        this(user.getUser(), user.getRoles());
    };

    public UserResponse(User user, List<Role> roles) {
        this.uuid = user.getUuid();
        this.name = user.getName();
        this.email = user.getEmail();
        this.roles = roles;
        this.automaticBilling = user.getAutomaticBilling();
    };

    public static List<UserResponse> fromList(
        List<User> list, 
        Map<UUID, List<Role>> mappedRoles
    ) {
        return list.stream()
            .map((user) -> {
                List<Role> roles = mappedRoles.getOrDefault(user.getUuid(), new LinkedList<>());
                return new UserResponse(user, roles);
            }).collect(Collectors.toList());
    };
};
