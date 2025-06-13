package app.netbooks.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import app.netbooks.backend.errors.UserNotFound;
import app.netbooks.backend.errors.ValidationsError;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.interfaces.UsersRepository;
import app.netbooks.backend.validation.Validator;

@Service
public class UsersService {
    @Autowired
    private UsersRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public List<User> findAll() {
        return this.repository.findAll();
    }; 

    public User findById(UUID uuid) throws UserNotFound {
        return this.repository.findById(uuid).orElseThrow(
            () -> new UserNotFound()
        );
    }; 

    public User login(
        String email, 
        String password
    ) throws UserNotFound {
        User user = this.repository.findByEmail(email).orElseThrow(
            () -> new UserNotFound()
        );

        if(!encoder.matches(password, user.getPassword()))
            throw new UserNotFound();
        
        return user;
    };

    public void register(
        String name, 
        String avatar,
        String email, 
        String password,
        String passwordConfirmation
    ) throws ValidationsError {
        Validator validator = new Validator();

        validator.validate("name", name)
            .min(3, "Tem mais de 2 caracteres!", "Tem menos de 3 caracteres!")
            .max(120, "Tem menos de 121 caracteres!", "Tem mais de 120 caracteres!")
            .pattern("^[A-Za-zÀ-ÿ ]*$", "Sem caracteres especiais!", "Caracteres especiais detectados!");
        
        validator.validate("email", email)
            .email("Formato válido!", "Fomato inválido!")
            .min(6, "Tem mais de 5 caracteres!", "Tem menos de 6 caracteres!")
            .max(120, "Tem menos de 121 caracteres!", "Tem mais de 120 caracteres!")
            .verify(!this.repository.findByEmail(email).isPresent(), "Disponível para uso!", "Já se encontra em uso!");

        validator.validate("password", password)
            .min(8, "Tem mais de 7 caracteres!", "Tem menos de 8 caracteres!")
            .max(24, "Tem menos de 25 caracteres!", "Tem mais de 24 caracteres!")
            .pattern("^\\S+$", "Sem espaços em branco!", "Espaços em branco detectados!")
            .pattern("(.*[A-Z].*){2,}", "Tem 2 letras maísculas!", "Precisa ter 2 letras maísculas!")
            .pattern("(.*[a-z].*){2,}", "Tem 2 letras minúsculas!", "Precisa ter 2 letras minúsculas!")
            .pattern("(.*[\\d].*){2,}", "Tem 2 dígitos!", "Precisa ter 2 dígitos!");

        validator.validate("passwordConfirmation", passwordConfirmation)
            .verify(passwordConfirmation != null && passwordConfirmation.equals(password), "Senhas coincidem!", "Senhas não coincidem!");

        validator.validate("avatar", avatar)
            .nullable()
            .pattern("^data:image\\/(png|jpeg|jpg);base64,[A-Za-z0-9+/]+={0,2}$", "Formato valido de imagem!", "Formato inválido de imagem!");

        validator.run();
        
        User user = new User(name, avatar, email, encoder.encode(password));
        this.repository.create(user);
    };

    public void update(
        User user,
        String name, 
        String avatar,
        String email, 
        String password
    ) throws ValidationsError {
        Validator validator = new Validator();

        validator.validate("name", name)
            .min(3, "Tem mais de 2 caracteres!", "Tem menos de 3 caracteres!")
            .max(120, "Tem menos de 121 caracteres!", "Tem mais de 120 caracteres!")
            .pattern("^[A-Za-zÀ-ÿ ]*$", "Sem caracteres especiais!", "Caracteres especiais detectados!");
    
        Optional<User> candidate = this.repository.findByEmail(email);
        validator.validate("email", email)
            .email("Formato válido!", "Fomato inválido!")
            .min(6, "Tem mais de 5 caracteres!", "Tem menos de 6 caracteres!")
            .max(120, "Tem menos de 121 caracteres!", "Tem mais de 120 caracteres!")
            .verify(!candidate.isPresent() || candidate.get().getEmail().equals(email), "Disponível para uso!", "Já se encontra em uso!");

        validator.validate("password", password)
            .min(8, "Tem mais de 7 caracteres!", "Tem menos de 8 caracteres!")
            .max(24, "Tem menos de 25 caracteres!", "Tem mais de 24 caracteres!")
            .pattern("^\\S+$", "Sem espaços em branco!", "Espaços em branco detectados!")
            .pattern("(.*[A-Z].*){2,}", "Tem 2 letras maísculas!", "Precisa ter 2 letras maísculas!")
            .pattern("(.*[a-z].*){2,}", "Tem 2 letras minúsculas!", "Precisa ter 2 letras minúsculas!")
            .pattern("(.*[\\d].*){2,}", "Tem 2 dígitos!", "Precisa ter 2 dígitos!");
        
        validator.validate("avatar", avatar)
            .nullable()
            .pattern("^data:image\\/(png|jpeg|jpg);base64,[A-Za-z0-9+/]+={0,2}$", "Formato valido de imagem!", "Formato inválido de imagem!");

        validator.run();
        
        user.setName(name);
        user.setAvatar(avatar);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        
        this.repository.update(user);
    };
};
