package app.netbooks.backend.files.images;

import java.nio.file.Path;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UsersAvatarsStorageImpl extends ImageStorage implements UsersAvatarsStorage {
    private Path getUsersAvatarsPath() {
        return this.getStorageFolder().resolve("users");
    };

    @Override
    public void storeAvatar(UUID uuid, MultipartFile file) {
        this.store(uuid.toString(), file, this.getUsersAvatarsPath());
    };

    @Override
    public void deleteAvatar(UUID uuid) {
        this.delete(uuid.toString(), this.getUsersAvatarsPath());
    };
};