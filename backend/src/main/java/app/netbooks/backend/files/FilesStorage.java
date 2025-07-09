package app.netbooks.backend.files;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

import app.netbooks.backend.errors.EmptyFile;
import app.netbooks.backend.errors.HttpError;
import app.netbooks.backend.errors.InternalServerError;

public abstract class FilesStorage {
    protected String mime;

    public FilesStorage(String mime) {
        this.mime = mime;
    };

    protected Path getStorageFolder() throws InternalServerError {
        try {
            Path path = Paths.get("").toAbsolutePath();

            String lastSegment = path.getFileName().toString();
            if(lastSegment.equals("backend") || lastSegment.equals("src")) {
                path = path.getParent();
            }
        
            return path.resolve("database").resolve("data");
        } catch (Exception e) {
            throw new InternalServerError();
        }
    };

    protected String makeFilename(
        String filenameWithoutMime
    ) {
        return filenameWithoutMime + "." + this.mime;
    };

    protected void delete(
        String filenameWithoutMime,
        Path path
    ) throws InternalServerError {
        try {
            Path filePath = path.resolve(this.makeFilename(filenameWithoutMime));
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            throw new InternalServerError();
        }
    };

    protected void store(
        String filenameWithoutMime, 
        MultipartFile file, 
        Path destiny
    ) throws InternalServerError, HttpError {
        try {
            this.validate(file);
            String filename = this.makeFilename(filenameWithoutMime);
            file.transferTo(destiny.resolve(filename));
        } catch(HttpError e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerError();
        };
    };

    public void validate(
        MultipartFile file
    ) throws EmptyFile {
        if(file.isEmpty()) throw new EmptyFile();
    };
};