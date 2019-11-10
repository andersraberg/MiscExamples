package se.anders_raberg.gradle_plugins.jacorb

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection

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
                String includeArgs = ""
                extension.idlDirs.each { includeArgs += "-I${it} "}
                String fileArgs = ""
                extension.idlFiles.each { fileArgs += it }

                project.exec {
                    commandLine 'sh', '-x', '-c', "/home/anders/IDLTest/jacorb-3.9/bin/idl -d ${project.buildDir}/generated/java ${includeArgs} ${fileArgs}"
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
