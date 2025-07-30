package org.example;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.provider.Property;

public interface ExampleExtension {
    Property<FileCollection> getFiles();

    DirectoryProperty getOutputDir();
}