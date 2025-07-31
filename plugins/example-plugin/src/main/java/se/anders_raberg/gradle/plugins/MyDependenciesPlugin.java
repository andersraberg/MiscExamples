package se.anders_raberg.gradle.plugins;

import java.util.Set;
import java.util.logging.Logger;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ResolvedDependency;

public class MyDependenciesPlugin implements Plugin<Project> {
    private static final Logger LOGGER = Logger.getLogger(MyDependenciesPlugin.class.getName());

    @Override
    public void apply(Project project) {
        project.getTasks().register("listAllDependencies", task -> task.doLast(t -> {
            LOGGER.info("Project: " + project.getName());
            for (Configuration configuration : project.getConfigurations()) {
                LOGGER.info("Configuration: " + configuration.getName());
                if (configuration.isCanBeResolved()) {
                    try {
                        configuration.resolve();
                        Set<ResolvedDependency> firstLevelDependencies = configuration.getResolvedConfiguration()
                                .getFirstLevelModuleDependencies();

                        LOGGER.info("Dependencies Tree:");
                        for (ResolvedDependency dependency : firstLevelDependencies) {
                            printDependencyTree(dependency, "  ");
                        }

                    } catch (Exception e) {
                        LOGGER.info("Unable to resolve configuration: " + e.getMessage());
                    }
                } else {
                    LOGGER.info("  [Skipped - Configuration cannot be resolved]");
                }
            }
        }));
    }

    /**
     * Recursively print the dependency tree.
     *
     * @param dependency The resolved dependency.
     * @param indent     The current indentation level for pretty printing.
     */
    private void printDependencyTree(ResolvedDependency dependency, String indent) {
        LOGGER.info(() -> String.format("%s%s:%s:%s", indent, dependency.getModuleGroup(), dependency.getModuleName(),
                dependency.getModuleVersion()));

        for (ResolvedDependency child : dependency.getChildren()) {
            printDependencyTree(child, indent + "  ");
        }
    }
}
