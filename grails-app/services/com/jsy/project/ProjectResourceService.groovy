package com.jsy.project

import com.jsy.auth.User
import com.jsy.system.UploadFile
import com.jsy.util.OrderProperty
import com.jsy.util.SearchProperty
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.grails.jaxrs.provider.DomainObjectNotFoundException
import grails.gorm.*
import org.json.JSONObject

class ProjectResourceService {
    WorkflowResourceService workflowResourceService

    def readAllForPage(String criteriaStr){
        JSONObject obj = JSON.parse(criteriaStr)

        def criterib = new DetachedCriteria(TSProject).build {
            //and
            Object andObj = obj.get("and-prperties")
            JSONArray array = (JSONArray)andObj;
            array.each{property->
                SearchProperty p =new SearchProperty(property);
                if ("between".equals(property.get("operate"))) {
                    between(p.key,new Long(p.gapValue1),new Long(p.gapValue2))
                }else if(p.value && !"".equals(p.value) && "like".equals(property.get("operate"))){
                    like(p.key,"%"+p.value+"%")
                }else if(p.value && !"".equals(p.value) && "eq".equals(property.get("operate"))){
                    eq(p.key,p.value)
                }
            }

            //or
            Object orObj = obj.get("or-prperties")
            JSONArray array2 = (JSONArray)orObj;
            if(array2.size()>0){
                or {
                    array2.each{property->
                        SearchProperty p =new SearchProperty(property);
                        if ("between".equals(property.get("operate"))) {
                            between(p.key,new Long(p.gapValue1),new Long(p.gapValue2))
                        }else if(p.value && !"".equals(p.value) && "like".equals(property.get("operate"))){
                            like(p.key,"%"+p.value+"%")
                        }else if(p.value && !"".equals(p.value) && "eq".equals(property.get("operate"))){
                            eq(p.key,p.value)
                        }
                    }
                }
            }

            //orderby
            Object orderByObj = obj.get("orderby-prperties")
            JSONArray array3 = (JSONArray)orderByObj;
            if(array3.size()>0){
                or {
                    array3.each{property->
                        OrderProperty p =new OrderProperty(property);
                        order(p.key,p.value)
                    }
                }
            }
        }

        def params = [:]
        params.max = Math.min(obj.get("page").max?.toInteger() ?: 25, 100)
        params.offset = obj.get("page").offset ? obj.get("page").offset.toInteger() : 0

        def results = criterib.list(params)
        def total = criterib.size()

        def rtn = [:]
        rtn.results=results
        rtn.total=total
        return rtn
    }


    def createProject(TSProject project){
        project.save(failOnError: true)
        workflowResourceService.createFlow(project.id)
    }

    def getAllFlowPhaseInfo(TSProject project,User user){

        def resultObj=[:]

        def projectWorkflows = project.projectWorkflows

        if(!projectWorkflows || projectWorkflows.size() ==0){
            return null
        }

        def projectWorkflow = projectWorkflows.getAt(0);
//        projectWorkflow = projectWorkflow.load(projectWorkflow.id)
        def workflowPhases = projectWorkflow.workflowPhases;

        /**
         * 只要是在项目实例的workflowPhases里面的，都可以显示
         */
        workflowPhases?.each {phase->
            def accessable = checkUserAccessable(phase, project, user);

            if(phase.phaseEn=='gatherInfo'){
                def relateFiles = []
                project.certificateFile?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    relateFiles << attachFile
                }

                def debtFiles = []
                project.debtFile?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    debtFiles << attachFile
                }

                def financialFiles = []
                project.financialFile?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    financialFiles << attachFile
                }

                def toPublicFiles = []
                project.toPublicFile?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    toPublicFiles << attachFile
                }

                def businessPlanFiles = []
                project.businessPlanFile?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    businessPlanFiles << attachFile
                }

                def other_attachments = []
                project.othersFiles.each{tsfile->
                    def entity = [:]
                    def files = []
                    entity.id = tsfile.id
                    entity.desc = tsfile.pdesc
                    tsfile.relateFiles?.each{file->
                        def attachFile = [:]
                        attachFile.fileName = file.fileName
                        attachFile.filePath = file.filePath
                        files << attachFile
                    }
                    entity.files = files;
                    other_attachments << entity
                }

                resultObj.gatherInfoBean = [
                    "phase": phase,
                    "accessable":accessable,
                    "certificateFilesDesc":project.certificateFile?.pdesc,
                    "debtFilesDesc":project.debtFile?.pdesc,
                    "financialFilesDesc":project.financialFile?.pdesc,
                    "toPublicFilesDesc":project.toPublicFile?.pdesc,
                    "businessPlanFilesDesc":project.businessPlanFile?.pdesc,
                    "certificateFiles_attachments": relateFiles,
                    "debtFiles_attachments": debtFiles,
                    "financialFiles_attachments": financialFiles,
                    "toPublicFiles_attachments": toPublicFiles,
                    "businessPlanFiles_attachments": businessPlanFiles,
                    "other_attachments": other_attachments
                ];
            }else if(phase.phaseEn=='gatherOA'){
                resultObj.gatherOABean = [
                        "phase": phase,
                        "accessable":accessable,
                        "project":project
                ];
            }else if(phase.phaseEn=='research'){
                resultObj.researchBean = [
                        "phase": phase,
                        "accessable":accessable,
                        "project":project
                ];
            }else if(phase.phaseEn=='researchOA'){
                resultObj.researchOABean = [
                        "phase": phase,
                        "accessable":accessable,
                        "project":project
                ];
            }else if(phase.phaseEn=='meeting'){
                resultObj.meetingBeans = [
                        "phase": phase,
                        "accessable":accessable,
                        "project":project
                ];
            }else if(phase.phaseEn=='otherEA'){
                resultObj.otherEABean = [
                        "phase": phase,
                        "accessable":accessable,
                        "project":project
                ];
            }else if(phase.phaseEn=='addCompany'){
                resultObj.addCompanyBean = [
                        "phase": phase,
                        "accessable":accessable,
                        "project":project
                ];
            }else if(phase.phaseEn=='makeContact'){
                resultObj.makeContactBean = [
                        "phase": phase,
                        "accessable":accessable,
                        "project":project
                ];
            }else if(phase.phaseEn=='makeContactOA'){
                resultObj.makeContactOABean = [
                        "phase": phase,
                        "accessable":accessable,
                        "project":project
                ];
            }

        }


        resultObj
    }

    def checkUserAccessable(phase, project, user){
        def isInAccessRole = checkInRole(phase.phaseParticipants,user.getAuthorities());
        def specailAccesses = checkSpecial(project,user,phase)
        def accessable = ( isInAccessRole || (specailAccesses!=null && specailAccesses.size()>0) ) && !phase.phaseFinished
        return accessable
    }

    def checkInRole(def phaseParticipants,def myRoles){
        boolean isIn=false;
        phaseParticipants?.each{role->
            myRoles?.each{myRole->
                if(role.name==myRole.name){
                    isIn = true;
                    return isIn;
                }
            }
        }
        isIn

    }

    def checkSpecial(def project,def user,def phase){
        if(!project || !user)
            return null


        def now = new Date();
        def c = SpecailAccess.createCriteria()
        def results = c {
            lt("fromDate", now)
            gt("toDate", now)
            eq("projectId", new Long(project.id).toInteger())
        }
        return results
    }


    def completeGather(TSProject project, def obj){
        println "otherFilesSize1:"+project.othersFiles.size()
        def phase = getPhase(project, "gatherInfo");

        if(obj.certificateFiles_attachments && obj.certificateFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.certificateFilesDesc,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.certificateFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.certificateFile){
                project.certificateFile.pdesc=flowFile.pdesc
                flowFile.relateFiles.each{it->
                    project.certificateFile.addToRelateFiles(it)
                }
            }else{
                project.certificateFile = flowFile
            }

        }
        println "otherFilesSize2:"+project.othersFiles.size()
        if(obj.debtFiles_attachments && obj.debtFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.debtFilesDesc,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.debtFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.debtFile){
                project.debtFile.pdesc=flowFile.pdesc
                flowFile.relateFiles.each{it->
                    project.debtFile.addToRelateFiles(it)
                }
            }else{
                project.debtFile = flowFile
            }
        }
        println "otherFilesSize3:"+project.othersFiles.size()
        if(obj.financialFiles_attachments && obj.financialFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.financialFilesDesc,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.financialFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.financialFile){
                project.financialFile.pdesc=flowFile.pdesc
                flowFile.relateFiles.each{it->
                    project.financialFile.addToRelateFiles(it)
                }
            }else{
                project.financialFile = flowFile
            }
        }
        println "otherFilesSize4:"+project.othersFiles.size()
        if(obj.toPublicFiles_attachments && obj.toPublicFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.toPublicFilesDesc,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.toPublicFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.toPublicFile){
                project.toPublicFile.pdesc=flowFile.pdesc
                flowFile.relateFiles.each{it->
                    project.toPublicFile.addToRelateFiles(it)
                }
            }else{
                project.toPublicFile = flowFile
            }
        }
        println "otherFilesSize5:"+project.othersFiles.size()
        if(obj.businessPlanFiles_attachments && obj.businessPlanFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.businessPlanFilesDesc,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.businessPlanFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.businessPlanFile){
                project.businessPlanFile.pdesc=flowFile.pdesc
                flowFile.relateFiles.each{it->
                    project.businessPlanFile.addToRelateFiles(it)
                }
            }else{
                project.businessPlanFile = flowFile
            }
        }
        println "otherFilesSize6:"+project.othersFiles.size()
        if(obj.other_attachments && obj.other_attachments.size() > 0){
            obj.other_attachments?.each{attachFile->
                TSFlowFile flowFile = new TSFlowFile(pdesc: attachFile.desc,flowPhase:phase,project:project);
                attachFile.files.each{otherFile->
                    UploadFile file = new UploadFile(fileName:otherFile.fileName,filePath:otherFile.filePath);
                    file.save(failOnError: true)
                    flowFile.addToRelateFiles(file)
                }
                flowFile.save(failOnError: true)

                project.addToOthersFiles(flowFile)
            }
        }

        println "otherFilesSize7:"+project.othersFiles.size()
        project.save(failOnError: true)
    }

    def getPhase(TSProject project,String phaseEn){
        def rtn
        project.getProjectWorkflow().workflowPhases.each {phase->
            if(phase.phaseEn==phaseEn){
                rtn = phase
                return rtn
            }
        }
        rtn
    }


}
