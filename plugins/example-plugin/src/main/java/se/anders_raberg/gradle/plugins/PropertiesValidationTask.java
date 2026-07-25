package se.anders_raberg.gradle.plugins;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CacheableTask
public abstract class PropertiesValidationTask extends DefaultTask {

    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    public abstract ConfigurableFileCollection getPropertiesFiles();

    @OutputFile
    public abstract RegularFileProperty getMarkerFile();

    @TaskAction
    void validate() throws IOException {
        Path markerFilePath = getMarkerFile().get().getAsFile().toPath();
        Files.deleteIfExists(markerFilePath);

        for (File file : getPropertiesFiles()) {
            getLogger().info("Validating {}", file.getAbsolutePath());

            try {
                new ValidatedProperties(file).load(Files.newBufferedReader(file.toPath()));
            } catch (IOException e) {
                throw new GradleException("Failed to load properties file " + file.getAbsolutePath(), e);
            }
        }

        Files.writeString(markerFilePath, "OK");
    }
}
