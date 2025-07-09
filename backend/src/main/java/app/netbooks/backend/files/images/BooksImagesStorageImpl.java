package app.netbooks.backend.files.images;

import java.nio.file.Path;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class BooksImagesStorageImpl extends ImageStorage implements BooksImagesStorage {
    private Path getBooksCoversPath() {
        return this.getStorageFolder().resolve("books/covers");
    };

    private Path getBooksBannersPath() {
        return this.getStorageFolder().resolve("books/banners");
    };

    @Override
    public void storeCover(Long id, MultipartFile file) {
        this.store(id.toString(), file, this.getBooksCoversPath());
    };

    @Override
    public void deleteCover(Long id) {
        this.delete(id.toString(), this.getBooksCoversPath());
    };

    @Override
    public void storeBanner(Long id, MultipartFile file) {
        this.store(id.toString(), file, this.getBooksBannersPath());
    };

    @Override
    public void deleteBanner(Long id) {
        this.delete(id.toString(), this.getBooksBannersPath());
    };
};