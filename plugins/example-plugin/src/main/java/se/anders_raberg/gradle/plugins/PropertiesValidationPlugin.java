package se.anders_raberg.gradle.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.language.base.plugins.LifecycleBasePlugin;

public class PropertiesValidationPlugin implements Plugin<Project> {
    public static final String PROPERTIES_FILE_PATTERN = "**/*.properties";
    public static final String PROPERTIES_VALIDATION_EXTENSION_NAME = "propertiesValidation";
    public static final String PROPERTIES_VALIDATION_TASK_NAME = "propertiesValidation";
    public static final String PROPERTIES_VALIDATION_MARKER_FILENAME = "markerfile.txt";

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(JavaPlugin.class);
        PropertiesValidationExtension extension = project.getExtensions()
                .create(PROPERTIES_VALIDATION_EXTENSION_NAME, PropertiesValidationExtension.class);

        SourceSetContainer sourceSets = project.getExtensions().getByType(SourceSetContainer.class);
        extension.getPropertiesFiles().from(sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).getAllSource()
                .matching(spec -> spec.include(PROPERTIES_FILE_PATTERN)));

        TaskProvider<PropertiesValidationTask> validate = project.getTasks()
                .register(PROPERTIES_VALIDATION_TASK_NAME, PropertiesValidationTask.class, task -> {
                    task.getPropertiesFiles().from(extension.getPropertiesFiles());
                    task.getMarkerFile()
                            .set(project.getLayout().getBuildDirectory().file(PROPERTIES_VALIDATION_MARKER_FILENAME));
                });

        project.getTasks().named(LifecycleBasePlugin.CHECK_TASK_NAME).configure(task -> task.dependsOn(validate));
    }
}
