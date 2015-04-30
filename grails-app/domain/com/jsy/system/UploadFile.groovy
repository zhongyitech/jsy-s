package com.jsy.system

import com.jsy.auth.User
import com.jsy.project.TSFlowFile
import com.jsy.project.TSProject
import com.jsy.project.TSWorkflowPhase

class UploadFile {
    def springSecurityService

    //文档类型：方便下载
    String fileType;
//    byte[] fileData;

    //文档名称
    String fileName
    //文件提取标识
    String filePath

    /**common字段**/
    Date dateCreated
    Date lastUpdated
    User creator

    static belongsTo = [
            flowFile : TSFlowFile,
    ];

    static constraints = {
        fileType nullable: true
        flowFile nullable: true
        creator nullable: true
    }

    def beforeInsert() {
        this.creator = springSecurityService.getCurrentUser()
    }


}
