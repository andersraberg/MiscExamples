package se.anders_raberg.gradle_plugins.jacorb

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.jacorb.idl.parser

public class JacorbPlugin implements Plugin<Project> {
    private static class JacorbPluginExtension {
        FileCollection idlDirs
        FileCollection idlFiles
    }

    @Override
    public void apply(Project project) {
        def extension = project.extensions.create('jacorb', JacorbPluginExtension)
        project.mkdir "${project.buildDir}/generated/java"
        project.tasks.register("IDL") {
            doLast {
                String arguments = "-d ${project.buildDir}/generated/java "
                extension.idlDirs.each { arguments += "-I${it} "}
                extension.idlFiles.each { arguments += "${it} "}

                parser.initLogging()
                boolean ok = parser.compileAndHandle(arguments.split(" +"))
                if (!ok) {
                    throw new GradleException("Error when compiling IDL files")
                }
            }
        }

        project.afterEvaluate {
            project.'IDL'.inputs.files(extension.idlFiles)
            extension.idlDirs.each { project.'IDL'.inputs.dir(it) }
            project.'IDL'.outputs.dir "${project.buildDir}/generated/java"
        }

        project.sourceSets {
            main {
                java {
                    srcDirs "${project.buildDir}/generated/java"
                }
            }
        }

        project.tasks.compileJava.dependsOn project.'IDL'
    }
}
