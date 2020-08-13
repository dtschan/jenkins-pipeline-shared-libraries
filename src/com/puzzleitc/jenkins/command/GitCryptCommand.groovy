package com.puzzleitc.jenkins.command

import com.puzzleitc.jenkins.command.context.PipelineContext

class GitCryptCommand {

    private final PipelineContext ctx
    private boolean unlocked

    GitCryptCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        def credentialsId = ctx.stepParams.getOptional('credentialsId', null) as String
        def unlocked = false
        if (credentialsId) {
            ctx.info("-- git-crypt unlock --")
            try {
                ctx.withCredentials([ctx.file(credentialsId: credentialsId, variable: 'GIT_CRYPT_KEYFILE')]) {
                    ctx.sh script: 'git-crypt unlock $GIT_CRYPT_KEYFILE'
                    unlocked = true
                }
            } finally {
                if (unlocked) {
                    ctx.info("-- git-crypt lock --")
                    ctx.sh script: 'git-crypt lock'
                }
            }
        }
    }
}
