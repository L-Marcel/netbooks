package app.netbooks.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import app.netbooks.backend.errors.UserNotFound;
import app.netbooks.backend.errors.ValidationsError;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.UsersRepository;
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

    public void register(
        String name, 
        String email, 
        String password
    ) throws ValidationsError {
        Validator validator = new Validator();

        validator.validate("name", name)
            .min(3, "Nome deve conter pelo menos 3 caracteres!")
            .max(120, "Nome não deve conter mais de 120 caracteres!")
            .pattern("^[A-Za-zÀ-ÿ ]*$", "Evite caracteres especiais no nome!");
        
        validator.validate("email", email)
            .email("Forneça um e-mail válido!")
            .min(6, "Email deve conter pelo menos 3 caracteres!")
            .max(320, "Email não deve conter mais de 320 caracteres!")
            .verify(!repository.findByEmail(email).isPresent(), "Email já está em uso!");

        validator.validate("password", password)
            .min(8, "Senha deve conter pelo menos 8 caracteres!")
            .max(24, "Senha não deve conter mais de 24 caracteres!")
            .pattern("^\\S+$", "Senha não deve conter espaços em branco!")
            .pattern("(.*[A-Z].*){2,}", "Senha deve conter pelo menos duas letras maísculas!")
            .pattern("(.*[a-z].*){2,}", "Senha deve conter pelo menos duas letras minúsculas!")
            .pattern("(.*[\\d].*){2,}", "Senha deve conter pelo menos dois dígitos!");

        validator.run();
        
        User user = new User(name, email, encoder.encode(password));
        repository.create(user);
    };

    public User login(
        String email, 
        String password
    ) throws UserNotFound {
        User user = repository.findByEmail(email).orElseThrow(
            () -> new UserNotFound()
        );

        if(!encoder.matches(password, user.getPassword()))
            throw new UserNotFound();
        
        return user;
    };
};
