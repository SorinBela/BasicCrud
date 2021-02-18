package api.json;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonFileHandler {

    private final Path pathToTargetFile;

    /**
     * This method is an approach to remove the parameter {@code pathToJsonFile}
     * from all the other API method signatures to 1) have one place, where the file
     * name is handled and sanitized, 2) allow to specify which file to use as data
     * sink. Note that this is mostly a placeholder to showcase the approach.
     * Actually sanitizing the path String is much more elaborate and
     * platform-dependent.
     *
     * @param pathToFile - the String representation of the Path to the File where
     *                   our JSON data should be stored.
     */
    public JsonFileHandler(Path pathToFile) {

        if(Files.exists(pathToFile))
            throw new UncheckedIOException(new IOException("Json-File-Repository already exists!"));

        try {
            this.pathToTargetFile = Files.createFile(pathToFile);
            if(!Files.isWritable(pathToFile))
                throw new UncheckedIOException(new IOException("No Write-Access on target path!"));
            if(!Files.isReadable(pathToFile))
                throw new UncheckedIOException(new IOException("No Read-Access on target path!"));

            Files.write(this.pathToTargetFile, "[]".getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            throw new UncheckedIOException(new IOException("Could not create File for Json-Repository at " + pathToFile.toString()));
        }
    }

    public void deleteJsonFile() {
        try {
            Files.deleteIfExists(this.pathToTargetFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(new IOException("Could not delete Json-Repository at " + pathToTargetFile.toString()));
        }
    }

    public void save(String json) {
        try {
            Files.write(this.pathToTargetFile, json.getBytes(StandardCharsets.UTF_8));
        } catch(IOException ex) {
            ex.printStackTrace();
            throw new UncheckedIOException(new IOException("Could not write JSON into target file!"));
        }
    }

    public String load() {

        String content;

        try {
            content = Files.readString(this.pathToTargetFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(new IOException("Could not read JSON repository!"));
        }

        return content;
    }

}
