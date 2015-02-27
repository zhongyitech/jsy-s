package com.jsy.project

import com.jsy.fundObject.Fund
import com.jsy.system.UploadFile

class TSFlowFile {
    String pdesc;  // 备注栏
    String pdesc2; // 结论栏，有些情况是没有的

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
        pdesc2 nullable: true
    }
}
