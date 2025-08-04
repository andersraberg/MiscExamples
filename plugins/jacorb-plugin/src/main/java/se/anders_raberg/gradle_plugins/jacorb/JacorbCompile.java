package se.anders_raberg.gradle_plugins.jacorb;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
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
import org.jacorb.idl.parser;

@CacheableTask
public abstract class JacorbCompile extends DefaultTask {
    private static final String FILE_SUFFIX = ".idl";
    private static final String INCLUDE_SWITCH = "-I";
    private static final String OUTPUT_SWITCH = "-d";

    @Inject
    protected abstract FileSystemOperations getFileSystemsOperations();

    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    abstract Property<FileCollection> getIdlDirs();

    @OutputDirectory
    abstract DirectoryProperty getOutputDir();

    @TaskAction
    void compile() {
        getFileSystemsOperations().delete(spec -> spec.delete(getOutputDir()));
        Set<File> idlDirs = getIdlDirs().get().getFiles();
        List<String> arguments = new ArrayList<>();
        arguments.addAll(List.of(OUTPUT_SWITCH, getOutputDir().get().getAsFile().getPath()));
        arguments.addAll(idlDirs.stream().map(d -> INCLUDE_SWITCH + d.getName()).toList());
        arguments.addAll(idlDirs.stream().flatMap(d -> filesInDir(d).stream()).toList());

        boolean ok = parser.compileAndHandle(arguments.toArray(new String[0]));

        if (!ok) {
            throw new GradleException("Error when compiling IDL files: " + arguments);
        }
    }

    private static List<String> filesInDir(File dir) {
        return Arrays.stream(dir.listFiles(f -> f.getName().endsWith(FILE_SUFFIX))).map(File::getPath).toList();
    }

}
