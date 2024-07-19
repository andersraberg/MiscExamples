package org.example;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class ExamplePlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		ExampleExtension extension = project.getExtensions().create("example", ExampleExtension.class);

		project.getTasks().register("exampleTask", ExampleTask.class, task -> {
			task.getFiles().set(extension.getFiles());
			task.getOutputDir().set(extension.getOutputDir());
		});
	}
}
