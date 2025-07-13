package app.netbooks.backend.configurations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilesConfig implements WebMvcConfigurer {
    private static Logger logger = LoggerFactory.getLogger(FilesConfig.class);

    @Override
    public void addResourceHandlers(
        @NonNull ResourceHandlerRegistry registry
    ) {
        registry.addResourceHandler("/users/**")
            .addResourceLocations("file:database/data/users/")
            .addResourceLocations("file:../database/data/users/");
        registry.addResourceHandler("/books/covers/**")
            .addResourceLocations("file:database/data/books/covers/")
            .addResourceLocations("file:../database/data/books/covers/");
        registry.addResourceHandler("/books/banners/**")
            .addResourceLocations("file:database/data/books/banners/")
            .addResourceLocations("file:../database/data/books/banners/");
    };

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDirectories() {
        List<String> subDirectories = Arrays.asList(
            "books/banners",
            "books/covers",
            "books/files",
            "users"
        );

        try {
            Path rootPath = Paths.get("database/data");
            Files.createDirectories(rootPath);

            for (String subDir : subDirectories) {
                Path dirPath = rootPath.resolve(subDir);
                Files.createDirectories(dirPath);
                FilesConfig.logger.info("Directory " + dirPath + " created");
            };
        } catch (Exception e) {
            FilesConfig.logger.error(e.getMessage());
        };
    };
};
