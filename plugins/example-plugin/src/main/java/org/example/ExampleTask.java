package org.example;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
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
public abstract class ExampleTask extends DefaultTask {
	@Inject
	protected abstract FileSystemOperations getFileSystemsOperations();

	@InputFiles
	@PathSensitive(PathSensitivity.RELATIVE)
	abstract Property<FileCollection> getFiles();

	@OutputDirectory
	abstract DirectoryProperty getOutputDir();

	@TaskAction
	void action() {
		getFileSystemsOperations().copy(s -> {
			s.from(getFiles());
			s.into(getOutputDir());
		});
	}

}
