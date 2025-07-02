package app.netbooks.backend.files.pdfs;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import app.netbooks.backend.errors.BookFileNotFound;

@Component
public class BookPdfsStorageImpl extends PdfsStorage implements BookPdfsStorage {
    private Path getBooksStoragePath() {
        return this.getStorageFolder().resolve("books/files");
    };
    
    @Override
    public void storeFile(Long id, MultipartFile file) {
        this.store(id.toString(), file, this.getBooksStoragePath());
    };

    @Override
    public void deleteFile(Long id) {
        this.delete(id.toString(), this.getBooksStoragePath());
    };

    @Override
    public Resource gerFile(Long id) {
        try {
            String filename = this.makeFilename(id.toString());
        
            Resource resource = new UrlResource(
                this.getBooksStoragePath().resolve(filename).toUri()
            );
            
            return resource;
        } catch (Exception e) {
            throw new BookFileNotFound();
        }
    };
};
