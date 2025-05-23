package app.netbooks.backend.images;

import java.nio.file.Path;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UserAvatarStorageImpl extends ImageStorage implements UserAvatarStorage {
    private Path getUsersAvatarsPath() {
        return this.getStorageFolder().resolve("users");
    };

    @Override
    public void storeAvatar(UUID uuid, String base64) {
        this.store(uuid.toString(), base64, this.getUsersAvatarsPath());
    };

    @Override
    public void deleteAvatar(UUID uuid) {
        this.delete(uuid.toString(), this.getUsersAvatarsPath());
    };
};