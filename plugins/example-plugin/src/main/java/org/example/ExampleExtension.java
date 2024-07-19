package org.example;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;

public interface ExampleExtension {
	Property<FileCollection> getFiles();

	DirectoryProperty getOutputDir();
}