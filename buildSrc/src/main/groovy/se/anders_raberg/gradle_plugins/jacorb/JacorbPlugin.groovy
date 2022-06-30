package se.anders_raberg.gradle_plugins.jacorb

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

public class JacorbPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        def extension = project.extensions.create('jacorb', JacorbExtension)
        def outputDir = project.layout.buildDirectory.dir("generated/java").get()
        project.mkdir outputDir
        
        TaskProvider jacorbCompile = project.tasks.register('jacorbCompile', JacorbCompile) {
            idlDirs = extension.idlDirs
            outputDirectory = outputDir
        }

        project.sourceSets {
            main {
                java {
                    srcDirs outputDir
                }
            }
        }

        project.tasks.compileJava.dependsOn jacorbCompile
    }
}
