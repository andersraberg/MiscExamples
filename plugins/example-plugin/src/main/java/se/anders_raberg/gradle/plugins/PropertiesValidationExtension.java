package se.anders_raberg.gradle.plugins;

import org.gradle.api.file.ConfigurableFileCollection;

public abstract class PropertiesValidationExtension {
    public abstract ConfigurableFileCollection getPropertiesFiles();
}