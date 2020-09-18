package com.puzzleitc.jenkins.command

import java.util.stream.Stream
import java.util.regex.Pattern
import java.nio.file.Paths
import java.nio.file.Files
import com.puzzleitc.jenkins.command.context.PipelineContext

class ExecutableCommand {

    private final PipelineContext ctx

    ExecutableCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    String findInPath(String executable) {
        for (def path: ctx.getEnv("PATH").split(Pattern.quote(File.pathSeparator))) {
            if (Files.isExecutable(Paths.get(path).resolve(executable))) {
                return path
            }
        }

        return null
/*         exePath = Stream.of(ctx.getEnv("PATH").split(Pattern.quote(File.pathSeparator)))
            .map(Paths.&get)
            .filter{ Files.isExecutable(it.resolve(executable)) }.findFirst().orElse(null) */
    }

    Object execute() {
        def executable = ctx.stepParams.getRequired("name") as String
        def toolName = ctx.stepParams.getRequired("toolName") as String

        def exePath = ctx.getEnv("${executable}_PATH")
        if (exePath) {
            return exePath
        }

        exePath = findInPath(executable)

        if (!exePath) {
            def toolHome = Paths.get(ctx.tool(toolName ? toolName : executable))
            if (Files.isExecutable(toolHome.resolve("bin").resolve(executable))) {
                exePath = toolHome.resolve("bin").toString()
            } else {
                exePath = toolHome.toString()
            }
            // toolHome = null
        }

        ctx.setEnv("${executable}_PATH", exePath)

        return exePath;
    }
}
