package app.netbooks.backend.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.models.Role;
import app.netbooks.backend.repositories.interfaces.RolesRepository;

@Service
public class RolesService {
    @Autowired
    private RolesRepository repository;

    public Map<UUID, List<Role>> mapAllByUser() {
        return this.repository.mapAllByUser();
    };

    public List<Role> findAllByUser(UUID uuid) {
        return this.repository.findAllByUser(uuid);
    };
};
