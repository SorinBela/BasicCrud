package api.json;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class JsonFileHandlerTest {

    private static final String HOME = System.getProperty("user.home");

    @Test
    void testCreateAndDelete() {
        Path filePath = Paths.get(HOME + "/json-repository.json");
        JsonFileHandler fileHandler = new JsonFileHandler(filePath);
        assertTrue(Files.exists(filePath));
        fileHandler.deleteJsonFile();
        assertTrue(Files.notExists(filePath));
    }

}