package se.anders_raberg.gradle_plugins.jacorb

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction;
import org.jacorb.idl.parser

class JacorbCompile extends DefaultTask {
    @InputFiles
    FileCollection idlDirs
    @OutputDirectory
    Directory outputDirectory

    @TaskAction
    def compile() {
        def arguments = "-d ${outputDirectory} "
        idlDirs.each { arguments += "-I${it} "}
        idlDirs.each {
            it.listFiles().each { arguments += "${it} " }
        }

        parser.initLogging()
        boolean ok = parser.compileAndHandle(arguments.split(" +"))
        if (!ok) {
            throw new GradleException("Error when compiling IDL files: ${arguments}")
        }
    }
}
