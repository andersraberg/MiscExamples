package se.anders_raberg.gradle.plugins;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.provider.Property;

public interface MixedCopyExtension {
    Property<FileCollection> getFiles();

    DirectoryProperty getOutputDir();
}