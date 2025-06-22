package se.anders_raberg.gradle.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class MixedCopyPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        MixedCopyExtension extension = project.getExtensions().create("mixedcopy", MixedCopyExtension.class);

        project.getTasks().register("mixedCopy", MixedCopyTask.class, task -> {
            task.getInputFiles().set(extension.getFiles());
            task.getOutputDir().set(extension.getOutputDir());
        });

    }
}
