package app.netbooks.backend;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class TestImages {
    private static Path getTestImagesPaths() {
        return Paths.get("../database/data/tests").toAbsolutePath();
    };

    public static String getTestImageBase64(String mime) {
        try {
            File file = TestImages.getTestImagesPaths().resolve("test." + mime).toFile();
            byte[] bytes = Files.readAllBytes(file.toPath());
            String base64 = Base64.getEncoder().encodeToString(bytes);
            return "data:image/" + mime + ";base64," + base64;
        } catch (Exception e) {
            return "";
        }
    };
};
