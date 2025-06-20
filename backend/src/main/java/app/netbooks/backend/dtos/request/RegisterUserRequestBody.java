package app.netbooks.backend.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequestBody {
    private String name;
    private String avatar;
    private String email;
    private String password;
    private String passwordConfirmation;
};