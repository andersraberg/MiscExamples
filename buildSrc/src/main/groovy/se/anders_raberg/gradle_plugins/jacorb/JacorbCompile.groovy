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
        List<String> arguments = ["-d", outputDirectory.asFile.toString()]
        idlDirs.each { arguments.add("-I" + it) }
        idlDirs.each {
            it.listFiles({File f -> f.name.endsWith(".idl")} as FileFilter).each { arguments.add(it.toString()) }
        }

        parser.initLogging()
        boolean ok = parser.compileAndHandle(arguments.toArray(new String[0]))
        if (!ok) {
            throw new GradleException("Error when compiling IDL files: " + arguments)
        }
    }
}
