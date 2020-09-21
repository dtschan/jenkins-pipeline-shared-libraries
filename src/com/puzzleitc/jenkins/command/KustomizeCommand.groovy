package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class KustomizeCommand {

    private final String resource
    private final PipelineContext ctx

    KustomizeCommand(String resource, PipelineContext ctx) {
        this.ctx = ctx
        this.resource = resource
    }

    Object execute() {
        ctx.info("-- kustomize --")
        ctx.echo("resource: $resource")
        def kustomizePath = ctx.executable("kustomize")
        ctx.sh(script: "${kustomizePath}/kustomize build ${resource}", returnStdout: true)
    }

}