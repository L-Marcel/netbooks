package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import app.netbooks.backend.models.Role;

public interface RolesRepository {
    public Map<UUID, List<Role>> mapAllByUser();
    public List<Role> findAllByUser(UUID uuid);
};
