package se.anders_raberg.gradle_plugins.jacorb;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.Directory;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;

public class JacorbPlugin implements Plugin<Project> {

	public static final String JACORB_GENERATED_JAVA_DIR = "generated/java";
	public static final String JACORB_EXTENSION_NAME = "jacorb";
	public static final String JACORB_COMPILE_TASK_NAME = "jacorbCompile";
	private static final String JACORB_DEPENDENCY = "org.jacorb:jacorb:3.9";

	@Override
	public void apply(Project project) {
		project.getPlugins().apply(JavaPlugin.class);
		JacorbExtension extension = project.getExtensions().create(JACORB_EXTENSION_NAME, JacorbExtension.class);
		Provider<Directory> outputDir = project.getLayout().getBuildDirectory().dir(JACORB_GENERATED_JAVA_DIR);
		TaskProvider<JacorbCompile> jacorbCompile = project.getTasks().register(JACORB_COMPILE_TASK_NAME,
				JacorbCompile.class, task -> {
					task.getIdlDirs().set(extension.getIdlDirs());
					task.getOutputDir().set(outputDir);
				});

		project.getDependencies().add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, JACORB_DEPENDENCY);
		project.getExtensions().getByType(SourceSetContainer.class).getByName(SourceSet.MAIN_SOURCE_SET_NAME).getJava()
				.srcDir(outputDir);

		project.getTasks().getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME).dependsOn(jacorbCompile);
	}
}
