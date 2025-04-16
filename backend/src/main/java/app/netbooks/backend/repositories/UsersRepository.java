package app.netbooks.backend.repositories;

import java.util.List;

import app.netbooks.backend.models.User;

public interface UsersRepository {
    public List<User> findAll();
};
