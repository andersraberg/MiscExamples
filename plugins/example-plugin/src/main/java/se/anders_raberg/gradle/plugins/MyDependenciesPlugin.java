package se.anders_raberg.gradle.plugins;

import java.util.Set;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ResolvedDependency;

public class MyDependenciesPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getTasks().register("listAllDependencies", task -> {
            task.doLast(t -> {
                System.out.println("Project: " + project.getName());
                for (Configuration configuration : project.getConfigurations()) {
                    System.out.println("Configuration: " + configuration.getName());
                    if (configuration.isCanBeResolved()) {
                        try {
                            configuration.resolve();
                            Set<ResolvedDependency> firstLevelDependencies = configuration.getResolvedConfiguration()
                                    .getFirstLevelModuleDependencies();

                            System.out.println("Dependencies Tree:");
                            for (ResolvedDependency dependency : firstLevelDependencies) {
                                printDependencyTree(dependency, "  ");
                            }

                        } catch (Exception e) {
                            System.out.println("Unable to resolve configuration: " + e.getMessage());
                        }
                    } else {
                        System.out.println("  [Skipped - Configuration cannot be resolved]");
                    }
                }
            });
        });
    }

    /**
     * Recursively print the dependency tree.
     *
     * @param dependency The resolved dependency.
     * @param indent     The current indentation level for pretty printing.
     */
    private void printDependencyTree(ResolvedDependency dependency, String indent) {
        System.out.println(indent + dependency.getModuleGroup() + ":" + dependency.getModuleName() + ":"
                + dependency.getModuleVersion());

        for (ResolvedDependency child : dependency.getChildren()) {
            printDependencyTree(child, indent + "  ");
        }
    }
}
