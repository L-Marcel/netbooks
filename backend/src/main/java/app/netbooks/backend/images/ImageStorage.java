package app.netbooks.backend.images;

import javax.imageio.ImageIO;

import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.errors.InvalidImageFormat;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public abstract class ImageStorage {
    public ImageStorage() {
        ImageIO.scanForPlugins();
    };

    protected Path getStorageFolder() {
        Path path = Paths.get("").toAbsolutePath();

        String lastSegment = path.getFileName().toString();
        if (lastSegment.equals("backend") || lastSegment.equals("src")) {
            path = path.getParent();
        }
    
        return path.resolve("database").resolve("data");
    };

    protected void delete(
        String filenameWithoutMime, 
        Path path
    ) throws InternalServerError {
        try {
            Path filePath = path.resolve(filenameWithoutMime + ".webp");
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            throw new InternalServerError();
        }
    };

    protected void store(
        String filenameWithoutMime, 
        String base64, 
        Path destiny
    ) throws InvalidImageFormat, InternalServerError {
        try {
            BufferedImage image = this.convertBase64ToBuffer(base64);
            String filename = filenameWithoutMime + ".webp";
            File file = destiny.resolve(filename).toFile();
            ImageIO.write(image, "webp", file);
        } catch (IOException e) {
            throw new InternalServerError();
        } catch (InvalidPathException e) {
            throw new InternalServerError();
        } catch (IllegalArgumentException e) {
            throw new InvalidImageFormat();
        } catch (IllegalStateException e) {
            throw new InternalServerError();
        };
    };

    private BufferedImage convertBase64ToBuffer(
        String base64
    ) throws IOException, IllegalArgumentException, InvalidImageFormat {
        String[] parts = base64.split(",");

        if(parts.length != 2) throw new InvalidImageFormat();

        this.validateBase64Header(parts[0]);
        byte[] bytes = Base64.getDecoder().decode(parts[1]);

        return ImageIO.read(new ByteArrayInputStream(bytes));
    };

    private void validateBase64Header(
        String base64Header
    ) throws InvalidImageFormat {
        if (
            base64Header.contains("jpeg") || 
            base64Header.contains("jpg") || 
            base64Header.contains("png")
        ) {
            return;
        };

        throw new InvalidImageFormat();
    };
};
