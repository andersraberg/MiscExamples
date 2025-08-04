package se.anders_raberg.gradle_plugins.jacorb;

import org.gradle.api.file.FileCollection;
import org.gradle.api.provider.Property;

public interface JacorbExtension {
    Property<FileCollection> getIdlDirs();
}