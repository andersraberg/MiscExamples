package se.anders_raberg.gradle.plugins;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.*;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.*;
import org.gradle.process.ExecOperations;

import javax.inject.Inject;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@CacheableTask
public abstract class RunScriptTask extends DefaultTask {

    @Inject
    protected abstract ExecOperations getExecOperations();

    @Input
    public abstract Property<String> getArg();

    @OutputDirectory
    public abstract DirectoryProperty getOutputDir();

    @TaskAction
    public void process() throws Exception {
        Path script = Files.createTempFile("myscript-", ".sh");
        try (InputStream in = MixedCopyPlugin.class.getResourceAsStream("/scripts/myscript.sh")) {
            Files.copy(in, script, StandardCopyOption.REPLACE_EXISTING);
            getExecOperations().exec(spec -> spec.commandLine("bash", script.toString(), getArg().get(),
                    getOutputDir().get().getAsFile().getAbsolutePath()));
        } finally {
            Files.deleteIfExists(script);
        }
    }

}
