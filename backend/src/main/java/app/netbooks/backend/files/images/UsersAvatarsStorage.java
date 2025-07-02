package app.netbooks.backend.files.images;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import app.netbooks.backend.errors.EmptyFile;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.errors.InvalidImageFormat;

public interface UsersAvatarsStorage {
    public void storeAvatar(UUID uuid, MultipartFile file) 
        throws InvalidImageFormat, InternalServerError, EmptyFile;  
    public void deleteAvatar(UUID uuid)
        throws InternalServerError;  
};
