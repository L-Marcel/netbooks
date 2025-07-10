package app.netbooks.backend.files.images;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import app.netbooks.backend.errors.EmptyFile;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.errors.InvalidImageFormat;
import app.netbooks.backend.files.FilesStorage;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;

public abstract class ImageStorage extends FilesStorage {
    public ImageStorage() {
        super("webp");
        ImageIO.scanForPlugins();
    };

    @Override
    protected void store(
        String filenameWithoutMime, 
        MultipartFile file,
        Path destiny
    ) throws InvalidImageFormat, InternalServerError, EmptyFile {
        try {
            this.validate(file);
            
            try (InputStream inputStream = file.getInputStream()) {
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                
                try(
                    FileOutputStream outputStream = new FileOutputStream(
                        destiny.resolve(filenameWithoutMime + "." + this.mime).toFile()
                    );
                ) {
                    ImageIO.write(bufferedImage, "webp", outputStream);
                };
            };
        } catch (Exception e) {
            throw new InternalServerError();
        };
    };

    @Override
    public void validate(
        MultipartFile file
    ) throws InvalidImageFormat, EmptyFile {
        super.validate(file);

        if(file.getContentType() == null) throw new InvalidImageFormat();
        
        String type = file.getContentType();
        Boolean isValid = 
            type.contains("jpeg") || 
            type.contains("jpg") || 
            type.contains("png") ||
            type.contains("webp");

        if(!isValid) throw new InvalidImageFormat();
    };
};
