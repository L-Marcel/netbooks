package app.netbooks.backend.dtos;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import app.netbooks.backend.models.Access;
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
    private Access access;

    public UserResponse(User user) {
        this.uuid = user.getUuid();
        this.name = user.getName();
        this.email = user.getEmail();
        this.access = user.getAccess();
    };

    public static List<UserResponse> fromList(List<User> list) {
        return list.stream()
            .map(UserResponse::new)
            .collect(Collectors.toList());
    };
};
