package app.netbooks.backend.images;

import java.util.UUID;

import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.errors.InvalidImageFormat;

public interface UserAvatarStorage {
    public void storeAvatar(UUID uuid, String base64) 
        throws InvalidImageFormat, InternalServerError;  
    public void deleteAvatar(UUID uuid)
        throws InternalServerError;  
};
