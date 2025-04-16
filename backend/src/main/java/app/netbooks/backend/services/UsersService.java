package app.netbooks.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.UsersRepository;

@Service
public class UsersService {
    @Autowired
    private UsersRepository repository;

    public List<User> findAll() {
        return this.repository.findAll();
    }; 
};
