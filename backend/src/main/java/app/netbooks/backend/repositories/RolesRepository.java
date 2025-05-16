package app.netbooks.backend.repositories;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import app.netbooks.backend.models.Role;

public interface RolesRepository {
    public Map<UUID, List<Role>> findAllRoles();
    public List<Role> findRoles(UUID uuid);
};
