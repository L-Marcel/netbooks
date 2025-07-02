package app.netbooks.backend.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilesConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(
        @SuppressWarnings("null") ResourceHandlerRegistry registry
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
};
