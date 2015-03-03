package jsy

import com.jsy.project.ProjectResourceService


class CheckOAJob {
    ProjectResourceService projectResourceService
    static triggers = {
        cron name: 'oaTrigger', cronExpression: "0/5 * * * * ?"
//      simple repeatInterval: 5000l // execute job once in 5 seconds
    }

    def execute() {
        projectResourceService.checkOAJob()
    }
}
