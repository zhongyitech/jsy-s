package com.jsy.project

import GsonTools.GsonTool
import Models.MsgModel
import Models.ProjectModelPhaseRole
import com.jsy.auth.User
import com.jsy.auth.UserRole
import com.jsy.fundObject.Fund
import com.jsy.fundObject.FundCompanyInformation
import com.jsy.system.Company
import com.jsy.auth.Role;
import com.jsy.system.UploadFile
import com.jsy.util.OrderProperty
import com.jsy.util.SearchProperty
import com.jsy.utility.MyResponse
import grails.converters.JSON
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONArray
import org.grails.jaxrs.provider.DomainObjectNotFoundException
import grails.gorm.*
import org.json.JSONObject

@Transactional(rollbackFor = Throwable.class)
class ProjectResourceService {
    public static String TAG = "ProjectResourceService ";

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
                }else if(p.value!=null && !"".equals(p.value) && "eq".equals(property.get("operate"))){
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
        resultObj.project = project.getProjectSimpleInfo();

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

                def analyseReportFiles = []
                project.analyseReportFile?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    analyseReportFiles << attachFile
                }

                def loanFiles = []
                project.loanFile?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    loanFiles << attachFile
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

                    "certificateFilesDesc":project.certificateFile?.pdesc,
                    "debtFilesDesc":project.debtFile?.pdesc,
                    "financialFilesDesc":project.financialFile?.pdesc,
                    "toPublicFilesDesc":project.toPublicFile?.pdesc,
                    "businessPlanFilesDesc":project.businessPlanFile?.pdesc,
                    "loanFilesDesc":project.loanFile?.pdesc,
                    "analyseReportFilesDesc":project.analyseReportFile?.pdesc,

                    "certificateFiles_attachments": relateFiles,
                    "debtFiles_attachments": debtFiles,
                    "financialFiles_attachments": financialFiles,
                    "toPublicFiles_attachments": toPublicFiles,
                    "businessPlanFiles_attachments": businessPlanFiles,
                    "loanFiles_attachments": loanFiles,
                    "analyseReportFiles_attachments": analyseReportFiles,
                    "other_attachments": other_attachments,

                    "phase": phase,
                    "accessable":accessable
                ];
            }else if(phase.phaseEn=='gatherOA'){
                def other_attachments = []
                project.oaGather_othersFiles.each{tsfile->
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

                resultObj.gatherOABean = [
                        "phase": phase,
                        "accessable":true,
                        "other_attachments": other_attachments,
                ];
            }else if(phase.phaseEn=='research'){

                def lawTransfer = []
                project.lawTransfer?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    lawTransfer << attachFile
                }

                def projectTransfer = []
                project.projectTransfer?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    projectTransfer << attachFile
                }

                def financialReport = []
                project.financialReport?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    financialReport << attachFile
                }

                def other_attachments = []
                project.researchOthersFiles.each{tsfile->
                    def entity = [:]
                    def files = []
                    entity.id = tsfile.id
                    entity.desc = tsfile.pdesc
                    entity.desc2 = tsfile.pdesc2
                    tsfile.relateFiles?.each{file->
                        def attachFile = [:]
                        attachFile.fileName = file.fileName
                        attachFile.filePath = file.filePath
                        files << attachFile
                    }
                    entity.files = files;
                    other_attachments << entity
                }



                resultObj.researchBean = [
                        "lawTransfer":lawTransfer,
                        "lawTransfer_pdesc":project.lawTransfer?.pdesc,
                        "lawTransfer_pdesc2":project.lawTransfer?.pdesc2,
                        "projectTransfer":projectTransfer,
                        "projectTransfer_pdesc":project.projectTransfer?.pdesc,
                        "projectTransfer_pdesc2":project.projectTransfer?.pdesc2,
                        "financialReport":financialReport,
                        "financialReport_pdesc":project.financialReport?.pdesc,
                        "financialReport_pdesc2":project.financialReport?.pdesc2,
                        "other_attachments":other_attachments,

                        "phase": phase,
                        "accessable":accessable
                ];
            }else if(phase.phaseEn=='researchOA'){

                def other_attachments = []
                project.oaResearch_othersFiles.each{tsfile->
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

                resultObj.researchOABean = [
                        "phase": phase,
                        "accessable":true,
                        "other_attachments": other_attachments,
                ];
            }else if(phase.phaseEn=='meeting'){
                def meetingRecord = []
                project.meetingRecord?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    meetingRecord << attachFile
                }

                def other_attachments = []
                project.meetingOthersFiles.each{tsfile->
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

                resultObj.meetingBeans = [
                        "meetingRecord":meetingRecord,
                        "meetingDesc": project.meetingOthersFiles?.pdesc,
                        "other_attachments":other_attachments,

                        "phase": phase,
                        "accessable":accessable
                ];
            }else if(phase.phaseEn=='otherEA'){
                def thirdPartyFile = []
                project.thirdPartyFile?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    thirdPartyFile << attachFile
                }

                def testFile = []
                project.testFile?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    testFile << attachFile
                }

                def houseFile = []
                project.houseFile?.relateFiles?.each{file->
                    def attachFile = [:]
                    attachFile.fileName = file.fileName
                    attachFile.filePath = file.filePath
                    houseFile << attachFile
                }

                def other_attachments = []
                project.thirdPartyOthersFiles.each{tsfile->
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

                resultObj.otherEABean = [
                        "thirdPartyFile":thirdPartyFile,
                        houseFile:houseFile,
                        testFile:testFile,

                        "thirdPartyDesc":project.thirdPartyFile?.pdesc,
                        "houseFileDesc":project.houseFile?.pdesc,
                        "testDesc":project.testFile?.pdesc,

                        "other_attachments":other_attachments,

                        "phase": phase,
                        "accessable":accessable
                ];
            }
//            else if(phase.phaseEn=='addCompany'){
//                resultObj.addCompanyBean = [
//                        "phase": phase,
//                        "accessable":accessable
//                ];
//            }
            else if(phase.phaseEn=='makeContact'){
                def signers = []
                project.signers?.each{signer->
                    def record = [:]
                    record.name = signer.name
                    record.value = signer.value
                    signers << record
                }

                def attentions = []
                project.attentions?.each{attention->
                    def record = [:]
                    record.name = attention.name
                    record.value = attention.value
                    attentions << record
                }


                def other_attachments = []
                project.makeContactOthersFiles.each{tsfile->
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



                resultObj.makeContactBean = [
                        "signers":signers,
                        "attentions":attentions,
                        "other_attachments":other_attachments,
                        "company":project.fund?.funcCompany?.companyName,
                        "fund":project.fund?.fundName,
                        "manage_per":project.manage_per,
                        "community_per":project.community_per,
                        "penalty_per":project.penalty_per,
                        "borrow_per":project.borrow_per,
                        interest_per:project.interest_per,
                        "year1":project.year1,
                        "year2":project.year2,
                        "interestType":project.interestType,
                        "daycount_per":project.daycount_per,
                        "phase": phase,
                        "accessable":accessable
                ];
            }else if(phase.phaseEn=='makeContactOA'){

                def other_attachments = []
                project.oaMakeContact_othersFiles.each{tsfile->
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

                resultObj.makeContactOABean = [
                        "phase": phase,
                        "accessable":project.isEnded,
                        "other_attachments": other_attachments,
                ];

            }

        }


        resultObj
    }

    /**
     * 此phase一定存在，不会对未来的phase进行判断
     * @param phase
     * @param project
     * @param user
     * @return
     */
    def checkUserAccessable(phase, project, user){
        //统一用model的phaseParticipants
        def modelPhase = TSWorkflowModelPhase.findByPhaseIndex(phase.phaseIndex)
        def isInAccessRole = checkInRole(modelPhase.phaseParticipants,user.getAuthorities());
        def specailAccesses = checkSpecial(project,user,phase)

        //如果是历史节点，需要看看他是不是ProjectHistoryModifier
        def historyAccesses = false;
        if(project.currentStageEn!=phase.phaseEn){
            def projectHistoryModifier = Role.findByAuthority("ProjectHistoryModifier")
            if(projectHistoryModifier){
                historyAccesses = UserRole.exists(user.id, projectHistoryModifier.id)
            }
            if(historyAccesses){
                return true
            }
        }

        //在本节点角色中，特殊时限，流程未结束
        def accessable = ( isInAccessRole || (specailAccesses!=null && specailAccesses.size()>0) ) && !phase.phaseFinished
        return accessable
    }

    //和上面的方法相比，就少了phase.phaseFinished判断
    def checkUserAccessable2(phase, project, user){
        //统一用model的phaseParticipants
        def modelPhase = TSWorkflowModelPhase.findByPhaseIndex(phase.phaseIndex)
        def isInAccessRole = checkInRole(modelPhase.phaseParticipants,user.getAuthorities());
        def specailAccesses = checkSpecial(project,user,phase)

        //如果是历史节点，需要看看他是不是ProjectHistoryModifier
        def historyAccesses = false;
        if(project.currentStageEn!=phase.phaseEn){
            def projectHistoryModifier = Role.findByAuthority("ProjectHistoryModifier")
            if(projectHistoryModifier){
                historyAccesses = UserRole.exists(user.id, projectHistoryModifier.id)
            }
            if(historyAccesses){
                return true
            }
        }

        //在本节点角色中，特殊时限，流程未结束
        def accessable = ( isInAccessRole || (specailAccesses!=null && specailAccesses.size()>0) )
        return accessable
    }

    def checkInRole(def phaseParticipants,def myRoles){
        boolean isIn=false;
        phaseParticipants?.each{role->
            myRoles?.each{myRole->
                if(role.authority==myRole.authority){
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
            if(phase){
                eq("phaseIndex", new Long(phase?.phaseIndex).toInteger())
            }
            eq("projectId", new Long(project.id).toInteger())
            eq("accessor", new Long(user?.id).toInteger())

        }
        return results
    }


    def completeGather(TSProject project, def obj){
        def phase = project.getProjectWorkflow().getGatherInfo();

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
        if(obj.loanFiles_attachments && obj.loanFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.loanFilesDesc,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.loanFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.loanFile){
                project.loanFile.pdesc=flowFile.pdesc
                flowFile.relateFiles.each{it->
                    project.loanFile.addToRelateFiles(it)
                }
            }else{
                project.loanFile = flowFile
            }
        }
        if(obj.analyseReportFiles_attachments && obj.analyseReportFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.analyseReportFilesDesc,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.analyseReportFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.analyseReportFile){
                project.analyseReportFile.pdesc=flowFile.pdesc
                flowFile.relateFiles.each{it->
                    project.analyseReportFile.addToRelateFiles(it)
                }
            }else{
                project.analyseReportFile = flowFile
            }
        }
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


        //设置下一个阶段，对于当前节点则推动到下一节点，否则都是对历史操作不动
        if(project.currentStageEn==phase.phaseEn){
            TSWorkflow tsWorkflow = project.getProjectWorkflow()
            tsWorkflow.moveToModelPhase(tsWorkflow.getGatherOAPhase())
            project.save(failOnError: true)
        }

    }


    def completeResearch(TSProject project, def obj) {
        def phase = project.getProjectWorkflow().getResearch()

        if(obj.lowFiles_attachments && obj.lowFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.lowDesc,pdesc2: obj.lowDesc2,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.lowFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.lawTransfer){
                project.lawTransfer.pdesc=flowFile.pdesc
                project.lawTransfer.pdesc2=flowFile.pdesc2
                flowFile.relateFiles.each{it->
                    project.lawTransfer.addToRelateFiles(it)
                }
            }else{
                project.lawTransfer = flowFile
            }

        }

        if(obj.projectFiles_attachments && obj.projectFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.projectDesc,pdesc2: obj.projectDesc2,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.projectFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.projectTransfer){
                project.projectTransfer.pdesc=flowFile.pdesc
                project.projectTransfer.pdesc2=flowFile.pdesc2
                flowFile.relateFiles.each{it->
                    project.projectTransfer.addToRelateFiles(it)
                }
            }else{
                project.projectTransfer = flowFile
            }

        }


        if(obj.finanFiles_attachments && obj.finanFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.finanDesc,pdesc2: obj.finanDesc2,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.finanFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.financialReport){
                project.financialReport.pdesc=flowFile.pdesc
                project.financialReport.pdesc2=flowFile.pdesc2
                flowFile.relateFiles.each{it->
                    project.financialReport.addToRelateFiles(it)
                }
            }else{
                project.financialReport = flowFile
            }

        }

        if(obj.research_other_attachments && obj.research_other_attachments.size() > 0){
            obj.research_other_attachments?.each{attachFile->
                TSFlowFile flowFile = new TSFlowFile(pdesc: attachFile.desc,pdesc2: attachFile.desc2,flowPhase:phase,project:project);
                attachFile.files.each{otherFile->
                    UploadFile file = new UploadFile(fileName:otherFile.fileName,filePath:otherFile.filePath);
                    file.save(failOnError: true)
                    flowFile.addToRelateFiles(file)
                }
                flowFile.save(failOnError: true)

                project.addToResearchOthersFiles(flowFile)
            }
        }

        //设置下一个阶段
        if(project.currentStageEn==phase.phaseEn){
            TSWorkflow tsWorkflow = project.getProjectWorkflow()
            def nextphase = tsWorkflow.getResearchOAPhase()
            tsWorkflow.moveToModelPhase(nextphase)
            project.save(failOnError: true)
        }

    }


    def completeMeeting(TSProject project, def obj) {
        def phase = project.getProjectWorkflow().getMeeting()

        if(obj.meetingFiles_attachments && obj.meetingFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.meetingDesc,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.meetingFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.meetingRecord){
                project.meetingRecord.pdesc=flowFile.pdesc
                flowFile.relateFiles.each{it->
                    project.meetingRecord.addToRelateFiles(it)
                }
            }else{
                project.meetingRecord = flowFile
            }

        }

        if(obj.meeting_other_attachments && obj.meeting_other_attachments.size() > 0){
            obj.meeting_other_attachments?.each{attachFile->
                TSFlowFile flowFile = new TSFlowFile(pdesc: attachFile.desc,pdesc2: attachFile.desc2,flowPhase:phase,project:project);
                attachFile.files.each{otherFile->
                    UploadFile file = new UploadFile(fileName:otherFile.fileName,filePath:otherFile.filePath);
                    file.save(failOnError: true)
                    flowFile.addToRelateFiles(file)
                }
                flowFile.save(failOnError: true)

                project.addToMeetingOthersFiles(flowFile)
            }
        }

        //设置下一个阶段
        if(project.currentStageEn==phase.phaseEn){
            TSWorkflow tsWorkflow = project.getProjectWorkflow()
            def nextphase = tsWorkflow.getOtherEAPhase()
            tsWorkflow.moveToModelPhase(nextphase)
            project.save(failOnError: true)
        }

    }

    def completeThirdpartyLow(TSProject project, def obj) {
        def phase = project.getProjectWorkflow().getOtherEA()

        if(obj.thirdpartyLowFiles_attachments && obj.thirdpartyLowFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.thirdpartyLowDesc,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.thirdpartyLowFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.thirdPartyFile){
                project.thirdPartyFile.pdesc=flowFile.pdesc
                flowFile.relateFiles.each{it->
                    project.thirdPartyFile.addToRelateFiles(it)
                }
            }else{
                project.thirdPartyFile = flowFile
            }

        }

        if(obj.houseFiles_attachments && obj.houseFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.houseFilesDesc,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.houseFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.houseFile){
                project.houseFile.pdesc=flowFile.pdesc
                flowFile.relateFiles.each{it->
                    project.houseFile.addToRelateFiles(it)
                }
            }else{
                project.houseFile = flowFile
            }
        }

        if(obj.testFiles_attachments && obj.testFiles_attachments.size() > 0){
            TSFlowFile flowFile = new TSFlowFile(pdesc: obj.testFilesDesc,flowPhase:phase,project:project);
            flowFile.save(failOnError: true)

            obj.testFiles_attachments?.each{attachFile->
                UploadFile file = new UploadFile(fileName:attachFile.fileName,filePath:attachFile.filePath);
                file.save(failOnError: true)
                flowFile.addToRelateFiles(file)
            }
            flowFile.save(failOnError: true)

            if(project.testFile){
                project.testFile.pdesc=flowFile.pdesc
                flowFile.relateFiles.each{it->
                    project.testFile.addToRelateFiles(it)
                }
            }else{
                project.testFile = flowFile
            }
        }

        if(obj.thirdpartyLow_other_attachments && obj.thirdpartyLow_other_attachments.size() > 0){
            obj.thirdpartyLow_other_attachments?.each{attachFile->
                TSFlowFile flowFile = new TSFlowFile(pdesc: attachFile.desc,pdesc2: attachFile.desc2,flowPhase:phase,project:project);
                attachFile.files.each{otherFile->
                    UploadFile file = new UploadFile(fileName:otherFile.fileName,filePath:otherFile.filePath);
                    file.save(failOnError: true)
                    flowFile.addToRelateFiles(file)
                }
                flowFile.save(failOnError: true)

                project.addToThirdPartyOthersFiles(flowFile)
            }
        }

        //设置下一个阶段
        if(project.currentStageEn==phase.phaseEn){
            TSWorkflow tsWorkflow = project.getProjectWorkflow()
            def nextphase = tsWorkflow.getMakeContactPhase()
            tsWorkflow.moveToModelPhase(nextphase)
            project.save(failOnError: true)
        }

    }

//    def completeAddCompany(TSProject project, def obj) {
//        FundCompanyInformation company =FundCompanyInformation.get(obj.companyid);
//        project.company=company
//
//        //设置下一个阶段
//        TSWorkflow tsWorkflow = project.getProjectWorkflow()
//        def nextphase = tsWorkflow.getMakeContactPhase()
//        tsWorkflow.moveToModelPhase(nextphase)
//        project.save(failOnError: true)
//    }

    def completeMakeContact(TSProject project, def obj) {
        MyResponse.ok {
            //检查是否关联了基金
            if(!project.fund){
                throw new Exception("项目没有关联基金")
            }

            def phase = project.getProjectWorkflow().getMakeContact()

            project.manage_per = obj.manage_per
            project.community_per = obj.community_per
            project.penalty_per = obj.notNormal_per
            project.borrow_per = obj.borrow_per
            project.daycount_per = obj.daycount_per
            project.interest_per = obj.interest_per

            project.year1 = Float.parseFloat(obj.year1)
            project.year2 = Float.parseFloat(obj.year2)
            project.interestType = obj.interestType

            obj.signers?.each{signer->
                if(signer.name && signer.value){
                    if(signer.name && signer.value){
                        SimpleRecord simpleRecord = new SimpleRecord(name: signer.name, value: signer.value)
                        simpleRecord.save(failOnError: true)
                        project.addToSigners(simpleRecord);
                    }
                }
            }

            obj.attentions?.each{attention->
                if(attention.name && attention.value){
                    SimpleRecord simpleRecord = new SimpleRecord(name: attention.name, value: attention.value)
                    simpleRecord.save(failOnError: true)
                    project.addToAttentions(simpleRecord);
                }
            }


            if(obj.other_attachments && obj.other_attachments.size() > 0){
                obj.other_attachments?.each{attachFile->
                    TSFlowFile flowFile = new TSFlowFile(pdesc: attachFile.desc,flowPhase:phase,project:project);
                    attachFile.files.each{otherFile->
                        UploadFile file = new UploadFile(fileName:otherFile.fileName,filePath:otherFile.filePath);
                        file.save(failOnError: true)
                        flowFile.addToRelateFiles(file)
                    }
                    flowFile.save(failOnError: true)

                    project.addToMakeContactOthersFiles(flowFile)
                }
            }

            //设置下一个阶段
            if(project.currentStageEn==phase.phaseEn){
                TSWorkflow tsWorkflow = project.getProjectWorkflow()
                def nextphase = tsWorkflow.makeContactOAPhase
                tsWorkflow.moveToModelPhase(nextphase)
                project.save(failOnError: true)
            }
        }


    }



    /**
     * gatherOA, researchOA，makeContactOA
     */
    def checkOAJob(){
        TSProject.list().each {project->
            def moveToModel = null
            def phase = project.getProjectWorkflow()?.getWorkflowCurrentPhase()
            if(!phase) return;
            if("gatherOA".equals(phase.phaseEn)){
                project.gatherOAStatus = "complete"

                TSWorkflow tsWorkflow = project.getProjectWorkflow()
                moveToModel = tsWorkflow.getResearchPhase()

                project.save(failOnError: true)
            }else if("researchOA".equals(phase.phaseEn)){
                project.researchOAStatus = "complete"

                TSWorkflow tsWorkflow = project.getProjectWorkflow()
                moveToModel = tsWorkflow.getMeetingPhase()

                project.save(failOnError: true)
            }else if("makeContactOA".equals(phase.phaseEn)){
                project.makeContactOAStatus = "complete"
                project.isEnded=true //已经是最后一个阶段了
                project.save(failOnError: true)
            }

            if(moveToModel){
//                println "move modelphase by job..."+moveToModel
                project.getProjectWorkflow().moveToModelPhase(moveToModel)
            }
        }
    }

    def completeOAGather(TSProject project, def obj){
        def phase = project.getProjectWorkflow().getGatherOA();

        if(obj.other_attachments && obj.other_attachments.size() > 0){
            obj.other_attachments?.each{attachFile->
                TSFlowFile flowFile = new TSFlowFile(pdesc: attachFile.desc,flowPhase:phase,project:project);
                attachFile.files.each{otherFile->
                    UploadFile file = new UploadFile(fileName:otherFile.fileName,filePath:otherFile.filePath);
                    file.save(failOnError: true)
                    flowFile.addToRelateFiles(file)
                }
                flowFile.save(failOnError: true)

                project.addToOaGather_othersFiles(flowFile)
            }
        }

        //设置下一个阶段
        if(project.currentStageEn==phase.phaseEn){
            TSWorkflow tsWorkflow = project.getProjectWorkflow()
            def nextphase = tsWorkflow.getResearchPhase()
            tsWorkflow.moveToModelPhase(nextphase)
            project.save(failOnError: true)
        }
    }

    def completeOAResearch(TSProject project, def obj){
        def phase = project.getProjectWorkflow().getResearchOA();

        if(obj.other_attachments && obj.other_attachments.size() > 0){
            obj.other_attachments?.each{attachFile->
                TSFlowFile flowFile = new TSFlowFile(pdesc: attachFile.desc,flowPhase:phase,project:project);
                attachFile.files.each{otherFile->
                    UploadFile file = new UploadFile(fileName:otherFile.fileName,filePath:otherFile.filePath);
                    file.save(failOnError: true)
                    flowFile.addToRelateFiles(file)
                }
                flowFile.save(failOnError: true)

                project.addToOaResearch_othersFiles(flowFile)
            }
        }

        //设置下一个阶段
        if(project.currentStageEn==phase.phaseEn){
            TSWorkflow tsWorkflow = project.getProjectWorkflow()

            def nextphase = tsWorkflow.getMeetingPhase()
            tsWorkflow.moveToModelPhase(nextphase)
            project.save(failOnError: true)
        }
    }

    def completeOAMakeContact(TSProject project, def obj){
        def phase = project.getProjectWorkflow().getMakeContactOA();

        if(obj.other_attachments && obj.other_attachments.size() > 0){
            obj.other_attachments?.each{attachFile->
                TSFlowFile flowFile = new TSFlowFile(pdesc: attachFile.desc,flowPhase:phase,project:project);
                attachFile.files.each{otherFile->
                    UploadFile file = new UploadFile(fileName:otherFile.fileName,filePath:otherFile.filePath);
                    file.save(failOnError: true)
                    flowFile.addToRelateFiles(file)
                }
                flowFile.save(failOnError: true)

                project.addToOaMakeContact_othersFiles(flowFile)
            }
        }

        //设置下一个阶段
        if(project.currentStageEn==phase.phaseEn){
            project.makeContactOAStatus = "complete"
            project.isEnded=true //已经是最后一个阶段了
            project.save(failOnError: true)
        }
    }



    /**
     * 添加限制访问列表
     * @param specailAccesses
     */
    def setSpecailAccess(SpecailAccess specailAccesses){
        MsgModel msgModel = null;
        if(!specailAccesses){
            msgModel = MsgModel.getErrorMsg(TAG + "SpecailAccess List Error, null pointer");
        }

        int projectId = specailAccesses.projectId;
        if(projectId == 0){
            msgModel = MsgModel.getErrorMsg(TAG + "add SpecailAccess Error projectId is 0");
        }

        int phaseIndex = specailAccesses.phaseIndex;
        if(phaseIndex == 0){
            msgModel = MsgModel.getErrorMsg(TAG + "add SpecailAccess Error phaseIndex is 0");

        }

        SpecailAccess specailAccess = SpecailAccess.findWhere(projectId: projectId,phaseIndex: phaseIndex);
        if(specailAccess){
            specailAccess.properties = specailAccesses;
            specailAccess.save(failOnError: true);

            if(specailAccess.hasErrors()){
                msgModel = MsgModel.getErrorMsg(TAG + specailAccess.getErrors().toString());
            }
        }else{

            specailAccesses.save(failOnError: true);
            if(specailAccesses.hasErrors()){
                msgModel = MsgModel.getErrorMsg(TAG + specailAccesses.getErrors().toString());
            }
        }

        if(msgModel != null){
            throw new Exception(msgModel.result);
        }
        return MsgModel.getSuccessMsg("save success");
    }

    /**
     * 获取项目模板某个节点的所有角色
     * @param phaseIndex
     * @return
     */
    def getProjectModelRole(int phaseIndex){
        MsgModel msgModel = null;
        TSWorkflowModel tsWorkflowModel = TSWorkflowModel.get(1);
        if(tsWorkflowModel == null){
            msgModel = MsgModel.getErrorMsg("can not find project model where project_id = 1");
        }

        TSWorkflowModelPhase tsWorkflowModelPhase = TSWorkflowModelPhase.findWhere(phaseIndex: phaseIndex);
        if(tsWorkflowModelPhase == null){
            msgModel = MsgModel.getErrorMsg("can not find workflow model phase where phase_index " + phaseIndex);
        }

        Set<Role> roles = tsWorkflowModelPhase.getPhaseParticipants();
        if(roles == null){
            msgModel = MsgModel.getErrorMsg("workflow model phase has no roles where phase_index " + phaseIndex);
        }

        if(msgModel != null){
            throw new Exception(msgModel.result);
        }
        String rolesJson = roles as JSON;
        ProjectModelPhaseRole projectModelPhaseRole = new ProjectModelPhaseRole(phaseIndex,rolesJson);

        return MsgModel.getSuccessMsg(GsonTool.getProjectModelPhaseRolesJson(projectModelPhaseRole));
    }

    /**
     * 删除项目节点的角色
     * @param phaseIndext
     */
    def removeProjectModelrRoles(int phaseIndex){
        MsgModel msgModel = null;
        TSWorkflowModelPhase tsWorkflowModelPhase = TSWorkflowModelPhase.findWhere(phaseIndex: phaseIndex);
        if(tsWorkflowModelPhase == null){
            msgModel = MsgModel.getErrorMsg("can not find work flow model phase where phase_index "+phaseIndex);
        }

        Set<Role> roles = tsWorkflowModelPhase.getPhaseParticipants();
        if(roles == null){
            msgModel = MsgModel.getErrorMsg("has no roles");
        }

        while(roles.iterator().hasNext()){
            Role role = roles.iterator().next();
            tsWorkflowModelPhase.removeFromPhaseParticipants(role);
        }

        if(msgModel != null){
            throw new Exception(msgModel.result);
        }

        return MsgModel.getSuccessMsg("delete success");
    }

    /**
     * 添加项目节点角色
     * @param id   角色ID
     * @param phaseIndex
     */
    def setProjectModelRole(Long id,int phaseIndex){
        MsgModel msgModel = null;
        Role role = Role.get(id);
        if(role == null){
            msgModel = MsgModel.getErrorMsg("can not find role where role_id "+id);
        }

        TSWorkflowModelPhase tsWorkflowModelPhase = TSWorkflowModelPhase.findWhere(phaseIndex: phaseIndex);
        if(tsWorkflowModelPhase == null){
            msgModel = MsgModel.getErrorMsg("can not find work flow model phase where phase_index "+phaseIndex);
        }

        tsWorkflowModelPhase.addToPhaseParticipants(role);
        tsWorkflowModelPhase.save(failOnError: true);

        if(tsWorkflowModelPhase.hasErrors()){
            msgModel = MsgModel.getErrorMsg(tsWorkflowModelPhase.getErrors());
        }

        if(msgModel != null){
            throw new Exception(msgModel.result);
        }
        return  MsgModel.getSuccessMsg("add success");
    }


}
