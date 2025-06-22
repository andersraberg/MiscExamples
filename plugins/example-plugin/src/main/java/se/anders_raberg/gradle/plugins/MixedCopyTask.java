package se.anders_raberg.gradle.plugins;

import java.io.File;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.ArchiveOperations;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileSystemOperations;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;

@CacheableTask
public abstract class MixedCopyTask extends DefaultTask {

    private static final String TAR_SUFFIX = ".tar";
    private static final String ZIP_SUFFIX = ".zip";

    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    public abstract Property<FileCollection> getInputFiles();

    @OutputDirectory
    public abstract DirectoryProperty getOutputDir();

    @Inject
    public abstract FileSystemOperations getFileSystemOperations();

    @Inject
    public abstract ArchiveOperations getArchiveOperations();

    @TaskAction
    public void performCopy() {
        Directory outputDir = getOutputDir().get();
        FileCollection inputFiles = getInputFiles().get();

        getFileSystemOperations().copy(spec -> {
            for (File file : inputFiles) {
                if (file.getName().endsWith(ZIP_SUFFIX)) {
                    spec.from(getArchiveOperations().zipTree(file));
                } else if (file.getName().endsWith(TAR_SUFFIX)) {
                    spec.from(getArchiveOperations().zipTree(file));
                } else {
                    spec.from(file);
                }
            }

            spec.into(outputDir);
        });
    }
}
