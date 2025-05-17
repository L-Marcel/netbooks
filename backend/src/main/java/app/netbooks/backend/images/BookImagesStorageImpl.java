package app.netbooks.backend.images;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

@Component
public class BookImagesStorageImpl extends ImageStorage implements BookImagesStorage {
    private Path getBooksCoversPath() {
        return Paths.get("../database/data/books/covers").toAbsolutePath();
    };

    private Path getBooksBannersPath() {
        return Paths.get("../database/data/books/banners").toAbsolutePath();
    };

    @Override
    public void storeCover(int id, String base64) {
        this.store(Integer.toString(id), base64, this.getBooksCoversPath());
    };

    @Override
    public void deleteCover(int id) {
        this.delete(Integer.toString(id), this.getBooksCoversPath());
    }

    @Override
    public void storeBanner(int id, String base64) {
        this.store(Integer.toString(id), base64, this.getBooksBannersPath());
    }

    @Override
    public void deleteBanner(int id) {
        this.delete(Integer.toString(id), this.getBooksBannersPath());
    };
};