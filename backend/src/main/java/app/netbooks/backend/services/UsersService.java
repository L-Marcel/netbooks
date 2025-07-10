package app.netbooks.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import app.netbooks.backend.connections.transactions.Transactions;
import app.netbooks.backend.errors.UserNotFound;
import app.netbooks.backend.errors.ValidationsError;
import app.netbooks.backend.files.images.UsersAvatarsStorage;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.interfaces.UsersRepository;
import app.netbooks.backend.validation.Validator;

@Service
public class UsersService {
    @Autowired
    private UsersRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UsersAvatarsStorage avatarsStorage;

    @Autowired
    private Transactions transactions;

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
        MultipartFile avatar,
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
            .verify((value) -> !this.repository.findByEmail(value).isPresent(), "Disponível para uso!", "Já se encontra em uso!");

        validator.validate("password", password)
            .min(8, "Tem mais de 7 caracteres!", "Tem menos de 8 caracteres!")
            .max(24, "Tem menos de 25 caracteres!", "Tem mais de 24 caracteres!")
            .pattern("^\\S+$", "Sem espaços em branco!", "Espaços em branco detectados!")
            .pattern("(.*[A-Z].*){2,}", "Tem 2 letras maísculas!", "Precisa ter 2 letras maísculas!")
            .pattern("(.*[a-z].*){2,}", "Tem 2 letras minúsculas!", "Precisa ter 2 letras minúsculas!")
            .pattern("(.*[\\d].*){2,}", "Tem 2 dígitos!", "Precisa ter 2 dígitos!");

        validator.validate("passwordConfirmation", passwordConfirmation)
            .verify((value) -> value.equals(password), "Senhas coincidem!", "Senhas não coincidem!");

        validator.validate("avatar", avatar)
            .nullable()
            .verifyIfCatch((value) -> avatarsStorage.validate(avatar), "Formato valido de imagem!", "Formato inválido de imagem!");
        
        validator.run();
        
        User user = new User(name, email, encoder.encode(password));

        this.transactions.run(() -> {
            this.repository.create(user);
            if(avatar != null) {
                avatarsStorage.storeAvatar(user.getUuid(), avatar);
            };
        }, () -> {
            avatarsStorage.deleteAvatar(user.getUuid());
        });
    };

    public void update(
        User user,
        String name, 
        MultipartFile avatar,
        String email, 
        String password,
        String passwordConfirmation,
        String oldPassword,
        Boolean updatePassword
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
            .verify((value) -> !candidate.isPresent() || candidate.get().getUuid().equals(user.getUuid()), "Disponível para uso!", "Já se encontra em uso!");

        validator.validate("oldPassword", oldPassword)
            .verify((value) -> encoder.matches(value, user.getPassword()), null, "Senha incorreta!");
        
        if(updatePassword) {
            validator.validate("password", password)
                .min(8, "Tem mais de 7 caracteres!", "Tem menos de 8 caracteres!")
                .max(24, "Tem menos de 25 caracteres!", "Tem mais de 24 caracteres!")
                .pattern("^\\S+$", "Sem espaços em branco!", "Espaços em branco detectados!")
                .pattern("(.*[A-Z].*){2,}", "Tem 2 letras maísculas!", "Precisa ter 2 letras maísculas!")
                .pattern("(.*[a-z].*){2,}", "Tem 2 letras minúsculas!", "Precisa ter 2 letras minúsculas!")
                .pattern("(.*[\\d].*){2,}", "Tem 2 dígitos!", "Precisa ter 2 dígitos!");
            
            validator.validate("passwordConfirmation", passwordConfirmation)
                .verify((value) -> value.equals(password), "Senhas coincidem!", "Senhas não coincidem!");
        };

        validator.validate("avatar", avatar)
            .nullable()
            .verifyIfCatch((value) -> avatarsStorage.validate(avatar), "Formato valido de imagem!", "Formato inválido de imagem!");
        
        validator.run();
        
        user.setName(name);
        user.setEmail(email);
        if(updatePassword) 
            user.setPassword(encoder.encode(password));
        
        this.transactions.run(() -> {
            this.repository.update(user);
            if(avatar != null) {
                avatarsStorage.storeAvatar(user.getUuid(), avatar);
            } else {
                avatarsStorage.deleteAvatar(user.getUuid());
            };
        });
    };
};
