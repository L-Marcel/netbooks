package app.netbooks.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import app.netbooks.backend.errors.EmailAlreadyInUse;
import app.netbooks.backend.errors.UserNotFound;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.UsersRepository;

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
    ) throws EmailAlreadyInUse {
        if(repository.findByEmail(email).isPresent()) 
            throw new EmailAlreadyInUse();
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
