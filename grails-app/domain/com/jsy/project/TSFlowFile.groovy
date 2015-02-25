package com.jsy.project

import com.jsy.fundObject.Fund
import com.jsy.system.UploadFile

class TSFlowFile {
    String pdesc;

    //common
    Date dateCreated
    Date lastUpdated

    static belongsTo = [
        flowPhase : TSWorkflowPhase,
        project: TSProject,
    ];

    static hasMany = [
        relateFiles : UploadFile,
    ];

    static constraints = {
        pdesc nullable: true
    }
}
