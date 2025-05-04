package app.netbooks.backend.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.User;

public interface UsersRepository {
    public List<User> findAll();
    public Optional<User> findByEmail(String email);
    public Optional<User> findById(UUID uuid);
    public void create(User user);
};
