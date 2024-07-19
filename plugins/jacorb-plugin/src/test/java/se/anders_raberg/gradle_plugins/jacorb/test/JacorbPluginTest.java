/*
 * This source file was generated by the Gradle 'init' task
 */
package se.anders_raberg.gradle_plugins.jacorb.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

import org.gradle.api.Project;
import org.gradle.language.base.plugins.LifecycleBasePlugin;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;

import se.anders_raberg.gradle_plugins.jacorb.JacorbPlugin;

class JacorbPluginTest {
	private static final String BUILD_SUCCESSFUL = "BUILD SUCCESSFUL";
	private static final String JACORB_PLUGIN_ID = "jacorb";
	private static final String BUILD_GRADLE = "build.gradle";

	@TempDir(cleanup = CleanupMode.NEVER)
	File projectDir;

	@Test
	void pluginRegistersATask() {
		Project project = ProjectBuilder.builder().build();
		project.getPlugins().apply(JACORB_PLUGIN_ID);

		assertNotNull(project.getTasks().findByName(JacorbPlugin.JACORB_COMPILE_TASK_NAME));
	}

	@Test
	void canRunTask() {
		Project project = ProjectBuilder.builder().build();
		project.copy(s -> {
			s.from(new File(getClass().getClassLoader().getResource(BUILD_GRADLE).getFile()).getParent());
			s.into(projectDir);
		});

		GradleRunner runner = GradleRunner.create();
		runner.forwardOutput();
		runner.withPluginClasspath();
		runner.withArguments(LifecycleBasePlugin.CLEAN_TASK_NAME, LifecycleBasePlugin.BUILD_TASK_NAME);
		runner.withProjectDir(projectDir);
		BuildResult result = runner.build();

		Set<String> of = Set.of(LifecycleBasePlugin.BUILD_TASK_NAME).stream().map(n -> ":" + n)
				.collect(Collectors.toSet());

		result.getTasks().stream().filter(t -> of.contains(t.getPath()))
				.forEach(t -> assertEquals(TaskOutcome.SUCCESS, t.getOutcome()));
	}
}
