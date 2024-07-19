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
	@Inject
	protected abstract FileSystemOperations getFileSystemsOperations();

	@InputFiles
	@PathSensitive(PathSensitivity.RELATIVE)
	abstract Property<FileCollection> getIdlDirs();

	@OutputDirectory
	abstract DirectoryProperty getOutputDir();

	@TaskAction
	void compile() {
		List<String> arguments = new ArrayList<>(List.of("-d", getOutputDir().get().toString()));
		Set<File> idlDirs = getIdlDirs().get().getFiles();
		idlDirs.forEach(d -> arguments.add("-I" + d.getName()));
		idlDirs.forEach(d -> Arrays.stream(d.listFiles()).filter(f -> f.getName().endsWith(".idl"))
				.forEach(f -> arguments.add(f.toString())));

		boolean ok = parser.compileAndHandle(arguments.toArray(new String[0]));

		if (!ok) {
			throw new GradleException("Error when compiling IDL files: " + arguments);
		}
	}

}
