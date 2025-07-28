package se.anders_raberg.gradle_plugins.jacorb.test;

import static org.gradle.api.plugins.BasePlugin.CLEAN_TASK_NAME;
import static org.gradle.testkit.runner.TaskOutcome.FROM_CACHE;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.anders_raberg.gradle_plugins.jacorb.JacorbPlugin.JACORB_COMPILE_TASK_NAME;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.gradle.testkit.runner.UnexpectedBuildFailure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;

@TestMethodOrder(OrderAnnotation.class)
class JacorbPluginTest {
    private static final String JACORB_PLUGIN_ID = "jacorb";

    @TempDir(cleanup = CleanupMode.ON_SUCCESS)
    private static Path buildTempDir;

    private static Path idlDirOne;
    private static Path idlDirTwo;

    @BeforeAll
    static void setupProject() throws IOException {
        idlDirOne = buildTempDir.resolve("one");
        idlDirTwo = buildTempDir.resolve("two");

        Files.writeString(buildTempDir.resolve("build.gradle"), """
                plugins {
                    id 'java'
                    id 'jacorb'
                }


                repositories {
                    mavenCentral()
                }

                jacorb {
                    idlDirs = files("%s", "%s")
                }
                """.formatted(idlDirOne, idlDirTwo));

        Files.createDirectories(idlDirOne);
        Files.writeString(idlDirOne.resolve("alpha.idl"), """
                interface Alpha {
                };
                """);

        Files.createDirectories(idlDirTwo);
        Files.writeString(idlDirTwo.resolve("bravo.idl"), """
                interface Bravo {
                };
                """);
    }

    @Test
    @Order(1)
    void testTaskRegistration() {
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply(JACORB_PLUGIN_ID);
        assertNotNull(project.getTasks().findByName(JACORB_COMPILE_TASK_NAME));
    }

    @Test
    @Order(2)
    void testCleanNoCache() {
        BuildResult result = runBuild(List.of(CLEAN_TASK_NAME, JACORB_COMPILE_TASK_NAME));
        assertEquals(SUCCESS, getOutcome(result, JACORB_COMPILE_TASK_NAME));
    }

    @Test
    @Order(3)
    void testUpToDate() {
        BuildResult result = runBuild(List.of(JACORB_COMPILE_TASK_NAME));
        assertEquals(UP_TO_DATE, getOutcome(result, JACORB_COMPILE_TASK_NAME));
    }

    @Test
    @Order(4)
    void testCleanWithCache() {
        BuildResult result = runBuild(List.of(CLEAN_TASK_NAME, JACORB_COMPILE_TASK_NAME));
        assertEquals(FROM_CACHE, getOutcome(result, JACORB_COMPILE_TASK_NAME));
    }

    @Test
    @Order(5)
    void testWithNewInputFile() throws IOException {
        Files.writeString(idlDirOne.resolve("charlie.idl"), """
                interface Charlie {
                };
                """);
        BuildResult result = runBuild(List.of(JACORB_COMPILE_TASK_NAME));
        assertEquals(SUCCESS, getOutcome(result, JACORB_COMPILE_TASK_NAME));
    }

    @Test
    @Order(6)
    void testJavaCompileOnGeneratedFiles() {
        BuildResult result = runBuild(List.of(JavaPlugin.COMPILE_JAVA_TASK_NAME));
        assertEquals(UP_TO_DATE, getOutcome(result, JACORB_COMPILE_TASK_NAME));
        assertEquals(SUCCESS, getOutcome(result, JavaPlugin.COMPILE_JAVA_TASK_NAME));
    }

    @Test
    @Order(7)
    void testJavaCompileWithAllUpToDate() {
        BuildResult result = runBuild(List.of(JavaPlugin.COMPILE_JAVA_TASK_NAME));
        assertEquals(UP_TO_DATE, getOutcome(result, JACORB_COMPILE_TASK_NAME));
        assertEquals(UP_TO_DATE, getOutcome(result, JavaPlugin.COMPILE_JAVA_TASK_NAME));
    }

    @Test
    @Order(8)
    void testWithNewBrokenInputFile() throws IOException {
        Files.writeString(idlDirOne.resolve("delta.idl"), """
                interBROKENface Delta {
                };
                """);

        List<String> tasks = List.of(JACORB_COMPILE_TASK_NAME);
        assertThrows(UnexpectedBuildFailure.class, () -> runBuild(tasks));
    }

    private static TaskOutcome getOutcome(BuildResult result, String taskName) {
        return result.task(":" + taskName).getOutcome();
    }

    private BuildResult runBuild(List<String> tasks) {
        List<String> tmpTasks = new ArrayList<>(tasks);
        tmpTasks.addAll(List.of("--build-cache", "--gradle-user-home", buildTempDir.resolve("gradle-home").toString()));

        return GradleRunner.create() //
                .withProjectDir(buildTempDir.toFile()) //
                .withPluginClasspath() //
                .withArguments(tmpTasks) //
                .withDebug(true) //
                .build();

    }
}
