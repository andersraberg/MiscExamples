package se.anders_raberg.gradle.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.language.base.plugins.LifecycleBasePlugin;

public class ValidationsPlugin implements Plugin<Project> {
    public static final String PROPERTIES_FILE_SUFFIX = ".properties";

    @Override
    public void apply(Project project) {
        TaskProvider<PropertiesValidationTask> validate = project.getTasks()
                .register("propertiesValidation", PropertiesValidationTask.class, task -> {
                    SourceSetContainer sourceSets = project.getExtensions().getByType(SourceSetContainer.class);
                    task.getPropertiesFiles().from(sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).getAllSource()
                            .filter(f -> f.getName().endsWith(PROPERTIES_FILE_SUFFIX)));

                    task.getMarkerFile().set(project.getLayout().getBuildDirectory().file("markerfile.txt"));
                });

        project.getTasks().named(LifecycleBasePlugin.CHECK_TASK_NAME).configure(task -> task.dependsOn(validate));
    }
}
