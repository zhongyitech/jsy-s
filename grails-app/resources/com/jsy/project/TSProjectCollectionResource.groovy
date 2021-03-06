package com.jsy.project

import GsonTools.GsonTool
import Models.MsgModel
import com.jsy.auth.User
import com.jsy.fundObject.Finfo
import com.jsy.fundObject.Fund
import com.jsy.system.UploadFile
import com.jsy.utility.CreateNumberService
import com.jsy.utility.JsonResult
import com.jsy.utility.MyResponse
import com.jsy.auth.Role;
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.DELETE
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam
import java.text.SimpleDateFormat

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/project')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class TSProjectCollectionResource {

    ProjectResourceService projectResourceService
    def springSecurityService

    /**
     json格式：
     { "and-prperties":[ {"name":"project","operate":"like"} ], "or-prperties":[], "page":{ "offset":0, "max":10},"orderby-prperties":[{"name":"desc"}] }

     * @param finfo
     * @return
     */
    @POST
    @Path('/readAllForPage')
    Response readAllForPage(String criteriaStr) {
        org.json.JSONObject result = new org.json.JSONObject();
        String restStatus = 200;
        int total
        def results
        try {
            if(criteriaStr && !"".equals(criteriaStr)){
                def rtn = projectResourceService.readAllForPage(criteriaStr)
                if(rtn){
                    total = rtn.total
                    results= rtn.results
                }
            }else{
                restStatus = "500";
            }
        }catch (Exception e){
            restStatus = "500";
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", results as JSON)
        result.put("rest_total", total)

        return Response.ok(result.toString()).status(200).build()
    }


    @GET
    @Path('/selectList')
    Response selectList(){
        com.jsy.utility.MyResponse.ok{
            def data=[]
            //todo:最好能优化为只从数据库中返回指定字段的方法
            TSProject.findAllByArchive(false).each{
                data.push([id:it.id,mapName:it.name,])
            }
            data
        }
    }

    //新增
    @POST
    @Path('/create')
    Response create(TSProject dto) {
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";

        try{

            projectResourceService.createProject(dto)

            result.put("rest_status", restStatus)
            result.put("rest_result", dto as JSON)
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            e.printStackTrace()
            print(e)
            result.put("rest_status", restStatus)
            result.put("rest_result", dto as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
    }



    @GET
    @Path('/nameLike')
    Response findByNameLike(@QueryParam('params') String name){
        def users=TSProject.findAllByNameLike("%"+name+"%")
        org.json.JSONArray jsonArray=new org.json.JSONArray()
        users.each {
            JSONObject jso=new JSONObject()
            jso.put("value",it.name)
            jso.put("data",it.id)
            jsonArray.put(jso)
        }
        JSONObject jsonObject=new JSONObject()
        jsonObject.put("query","Unit")
        jsonObject.put("suggestions",jsonArray)

        return Response.ok(jsonObject.toString()).status(200).build();
    }

    /**
     * 获取该项目的流程节点信息
     * @param projectid
     * @return
     */
    @GET
    @Path('/stepInfo')
    Response fetchStepInfo(@QueryParam('projectid') int projectid){
        MyResponse.ok {
            TSProject project=TSProject.get(projectid)
            if(!project){
                throw new Exception("no project found")
            }

            def allPhaseInfos = projectResourceService.getAllFlowPhaseInfo(project,springSecurityService.getCurrentUser());
            return allPhaseInfos
        }
    }

    @GET
    @Path('/projectSettingInfo')
    Response projectSettingInfo(@QueryParam('projectid') int projectid){
        MyResponse.ok {
            TSProject project=TSProject.get(projectid)
            if(!project){
                throw new Exception("no project found")
            }
            return project.getProjectSimpleInfo()
        }
    }


    //提交收集
    @POST
    @Path('/complete_gather')
    Response completeGather(String datastr) {
        MyResponse.ok {
            // get project
            org.json.JSONObject obj = JSON.parse(datastr)
            def projectid = obj.projectid
            TSProject project = TSProject.get(projectid);
            if(!project){
                throw new Exception("no such project found")
            }

            //权限校验
            def user = springSecurityService.getCurrentUser();
            def phase = project.getProjectWorkflow().getGatherInfo()
            def accessable = projectResourceService.checkUserAccessable(phase,project,user);
            if(!accessable){
                throw new Exception("you can not access this phase")
            }

            //数据保存
            projectResourceService.completeGather(project,obj)
        }
    }

    @POST
    @Path('/complete_oagather')
    Response completeOagather(String datastr) {
        MyResponse.ok {
            // get project
            org.json.JSONObject obj = JSON.parse(datastr)
            def projectid = obj.projectid
            TSProject project = TSProject.get(projectid);
            if(!project){
                throw new Exception("no such project found")
            }

            //权限校验
            def user = springSecurityService.getCurrentUser();
            def phase = project.getProjectWorkflow().getGatherInfo()
            def accessable = projectResourceService.checkUserAccessable2(phase,project,user);
            if(!accessable){
                throw new Exception("you can not access this phase")
            }

            //数据保存
            projectResourceService.completeOAGather(project,obj)
        }
    }

    @POST
    @Path('/complete_oaresearch')
    Response completeOaresearch(String datastr) {
        MyResponse.ok {
            // get project
            org.json.JSONObject obj = JSON.parse(datastr)
            def projectid = obj.projectid
            TSProject project = TSProject.get(projectid);
            if(!project){
                throw new Exception("no such project found")
            }

            //权限校验
            def user = springSecurityService.getCurrentUser();
            def phase = project.getProjectWorkflow().getResearch()
            def accessable = projectResourceService.checkUserAccessable2(phase,project,user);
            if(!accessable){
                throw new Exception("you can not access this phase")
            }

            //数据保存
            projectResourceService.completeOAResearch(project,obj)
        }
    }

    @POST
    @Path('/complete_oamakecontact')
    Response completeOamakecontact(String datastr) {
        MyResponse.ok {
            // get project
            org.json.JSONObject obj = JSON.parse(datastr)
            def projectid = obj.projectid
            TSProject project = TSProject.get(projectid);
            if(!project){
                throw new Exception("no such project found")
            }

            //权限校验
            def user = springSecurityService.getCurrentUser();
            def phase = project.getProjectWorkflow().getMakeContact()
            def accessable = projectResourceService.checkUserAccessable2(phase,project,user);
            if(!accessable){
                throw new Exception("you can not access this phase")
            }

            //数据保存
            projectResourceService.completeOAMakeContact(project,obj)
        }
    }


    //提交现场考察
    @POST
    @Path('/complete_research')
    Response completeResearch(String datastr) {
        MyResponse.ok {
            // get project
            org.json.JSONObject obj = JSON.parse(datastr)
            def projectid = obj.projectid
            TSProject project = TSProject.get(projectid);
            if(!project){
                throw new Exception("no such project found")
            }

            //权限校验
            def user = springSecurityService.getCurrentUser();
            def phase = project.getProjectWorkflow().getResearch()
            def accessable = projectResourceService.checkUserAccessable(phase,project,user);
            if(!accessable){
                throw new Exception("you can not access this phase")
            }

            //数据保存
            projectResourceService.completeResearch(project,obj)
        }
    }

    //提交会议
    @POST
    @Path('/complete_meeting')
    Response completeMeeting(String datastr) {
        MyResponse.ok {
            // get project
            org.json.JSONObject obj = JSON.parse(datastr)
            def projectid = obj.projectid
            TSProject project = TSProject.get(projectid);
            if(!project){
                throw new Exception("no such project found")
            }

            //权限校验
            def user = springSecurityService.getCurrentUser();
            def phase = project.getProjectWorkflow().getMeeting()
            def accessable = projectResourceService.checkUserAccessable(phase,project,user);
            if(!accessable){
                throw new Exception("you can not access this phase")
            }

            //数据保存
            projectResourceService.completeMeeting(project,obj)
        }
    }


    //提交第三方法律
    @POST
    @Path('/complete_thirdpartyLow')
    Response completeThirdpartyLow(String datastr) {

        MyResponse.ok {
            // get project
            org.json.JSONObject obj = JSON.parse(datastr)
            def projectid = obj.projectid
            TSProject project = TSProject.get(projectid);
            if(!project){
                throw new Exception("no such project found")
            }

            //权限校验
            def user = springSecurityService.getCurrentUser();
            def phase = project.getProjectWorkflow().getOtherEA()
            def accessable = projectResourceService.checkUserAccessable(phase,project,user);
            if(!accessable){
                throw new Exception("you can not access this phase")
            }

            //数据保存
            projectResourceService.completeThirdpartyLow(project,obj)
        }
    }


    //提交添加合作伙伴
    @POST
    @Path('/complete_makeContact')
    Response completeMakeContact(String datastr) {
//        println datastr
        MyResponse.ok {
            // get project
            org.json.JSONObject obj = JSON.parse(datastr)
            def projectid = obj.projectid
            TSProject project = TSProject.get(projectid);
            if(!project){
                throw new Exception("no such project found")
            }

            //权限校验
            def user = springSecurityService.getCurrentUser();
            def phase = project.getProjectWorkflow().getMakeContact()
            def accessable = projectResourceService.checkUserAccessable(phase,project,user);
            if(!accessable){
                throw new Exception("you can not access this phase")
            }

            //数据保存
            projectResourceService.completeMakeContact(project, obj)
            return true
        }
    }

    @POST
    @Path('/delFile')
    Response delFile(@QueryParam('fileName') String fileName,@QueryParam('filePath') String filePath) {
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";


        try{
            //校验
            if(!fileName || "".equals(fileName) || !filePath || "".equals(filePath)){
//                restStatus = "500";
//                result.put("rest_status", restStatus)
//                result.put("rest_result", "lack of fileName or filePath")
//                return Response.ok(result.toString()).status(500).build()
                return  Response.ok(JsonResult.success("lack of fileName or filePath")).build()
            }


            result.put("rest_status", restStatus)
            result.put("rest_result", "")
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            ok JsonResult.error(e.message)
        }

    }

    @POST
    @Path('/delTsFile')
    Response delTsFile(@QueryParam('id') int  id) {
//        JSONObject result = new JSONObject();
//        JSONArray table = new JSONArray();
//        String restStatus = "200";
        try{
//            projectResourceService.completeGather(project,obj)
//            result.put("rest_status", restStatus)
//            result.put("rest_result", "")
//            return Response.ok(result.toString()).status(200).build()
            ok JsonResult.success()
        }catch (Exception e){
//            restStatus = "500";
//            e.printStackTrace()
//            result.put("rest_status", restStatus)
//            result.put("rest_result", e.getLocalizedMessage())
//            return Response.ok(result.toString()).status(500).build()
        }
    }

    /**
     * @param projectId    项目ID
     * @return
     */
    @GET
    @Path('/getSpecailAccess')
    Response getSpecailAccess(@QueryParam("projectId") int projectId){

        MyResponse.ok {
            if(!TSProject.get(projectId)){
                throw new Exception("项目不存在！")
            }
            return SpecailAccess.findAllByProjectId(projectId)
        }

    }

    /**
     * 保存限时访问信息
     * @param specailAccesses
     * @return
     */
    @POST
    @Path('/setSpecailAccess')
    Response setSpecailAccess(String dataStr){
        MyResponse.ok {
            org.json.JSONObject obj = JSON.parse(dataStr)
            def projectid = obj.projectid
            TSProject project = TSProject.get(projectid)

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

            if(project){
                SpecailAccess.findAllByProjectId(projectid)?.each{
                    it.delete(flush: true)
                }
                obj.phaseDatas?.each{phaseData->
                    phaseData?.datas?.each{
                        def fromDate = sf.parse(it.fromDate)
                        def toDate = sf.parse(it.toDate)

                        SpecailAccess specailAccess = new SpecailAccess(projectId:projectid,phaseIndex: phaseData.phaseIndex, accessor:it.accessor,fromDate:fromDate,toDate:toDate);
                        specailAccess.save(failOnError: true)
                    }
                }
            }
        }
    }

    /**
     * 获取项目模板某个节点的所有角色
     * @param phaseIndex
     * @return
     */
    @GET
    @Path('/getProjectModelRole')
    Response getProjectModelRole(){
        MyResponse.ok {
            return TSWorkflowModelPhase.list()
        }
    }

    /**
     * 删除项目节点的角色
     * @param phaseIndex
     * @return
     */
    @DELETE
    @Path('removeProjectModelrRoles')
    Response removeProjectModelrRoles(@QueryParam("phaseIndex") int phaseIndex){
        try{
            MsgModel msgModel = projectResourceService.removeProjectModelrRoles(phaseIndex);
            if(msgModel == null){
                ok JsonResult.error("java error");
            }
            ok JsonResult.success(msgModel.result);
        }catch (Exception ex){
            ok JsonResult.error(ex.message);
        }
    }

    /**
     * 添加项目节点角色
     * @param id
     * @param phaseIndex
     * @return
     */
    @POST
    @Path('/setProjectModelRole')
    Response setProjectModelRole(@QueryParam("roleids") String roleids,@QueryParam("phaseIndex") int phaseIndex){
        MyResponse.ok {
            TSWorkflowModelPhase phase = TSWorkflowModelPhase.findByPhaseIndex(phaseIndex)
            Set roles = new ArrayList()
            roleids.replace("[","").replace("]","").replace("\"","").split(",").each {
                roles << Role.get(it)
            }
            roles = roles.unique()
            phase.setPhaseParticipants(roles)
            phase.save(failOnError: true)
        }
    }


    @GET
    @Path('/getProjectFromFund')
    Response getProjectFromFund(@QueryParam("fundId") int fundId){
        MyResponse.ok {
            Fund fund = Fund.get(fundId)
            if(fund){
                def rtn = [:]
                rtn.id = fund.project?.id
                rtn.name = fund.project?.name
                return rtn
            }else{
                return null
            }

        }
    }

    @GET
    @Path('/getProjectSimpleInterestInfo1')
    Response getProjectInterestType(@QueryParam("projectid") int projectid){
        MyResponse.ok {
            TSProject project = TSProject.get(projectid)
            if(project){
                def rtn = [:]
                rtn.interestType = project.interestType
                rtn.daycount_per = project.daycount_per
                return rtn
            }else{
                return null
            }

        }
    }

    @POST
    @Path('/endProject')
    Response endProject(@QueryParam("id") int projectid,@QueryParam("mark") String mark,@QueryParam("endProjectFiles") JSONArray endProjectFiles){
        MyResponse.ok {
            TSProject project = TSProject.get(projectid)
            if(project){
                project.archive = true
                project.isEnded = true
                project.endSummary = mark

                endProjectFiles.each {file->
                    UploadFile temp = new UploadFile(fileName:file.fileName,filePath:file.filePath)
                    project.addToEndProjectFiles(temp)
                }


                project.save(failOnError: true)
                return "done"
            }else{
                return null
            }

        }
    }

    @POST
    @Path('/updateProjectDaycount_per')
    Response updateProjectDaycount_per(@QueryParam("daycount_per") BigDecimal daycount_per,@QueryParam("projectid") int projectid){
        MyResponse.ok {
            TSProject project = TSProject.get(projectid)
            if(project){
                project.daycount_per = daycount_per
                project.save(failOnError: true)
                return "done"
            }else{
                return null
            }

        }
    }

    @POST
    @Path('/saveProjectInterestType')
    Response saveProjectInterestType(@QueryParam("interestType") String interestType,@QueryParam("projectid") int projectid){
        MyResponse.ok {
            TSProject project = TSProject.get(projectid)
            if(project){
                project.interestType = interestType
                project.save(failOnError: true)
                return "done"
            }else{
                return null
            }

        }
    }

    @POST
    @Path('/saveProjectSettings')
    Response saveProjectSettings(String datastr){
        MyResponse.ok {
            org.json.JSONObject obj = JSON.parse(datastr)
            TSProject project = TSProject.get(obj.projectid)
            if(project){
                def interest_per = obj.interest_per
                def daycount_per = obj.daycount_per
                def interestType = obj.interestType
                def manage_per = obj.manage_per
                def community_per = obj.community_per
                def penalty_per = obj.penalty_per
                def borrow_per = obj.borrow_per
                def year1 = obj.year1
                def year2 = obj.year2
                def fund=Fund.get(obj.fund.id)

                if(interest_per)
                    project.interest_per = interest_per
                if(daycount_per)
                    project.daycount_per = daycount_per
                if(interestType)
                    project.interestType = interestType
                if(manage_per)
                    project.manage_per = manage_per
                if(community_per)
                    project.community_per = community_per
                if(penalty_per)
                    project.penalty_per = penalty_per
                if(borrow_per)
                    project.borrow_per = borrow_per
                if(year1)
                    project.year1 = year1
                if(year2)
                    project.year2 = year2
                if(fund){
                    project.fund = fund
                    fund.project = project
                    fund.save(failOnError: true)
                }
                project.save(failOnError: true)
                return true
            }else{
                throw new Exception("no project found")
            }

        }
    }



    @GET
    @Path('/getProjectSimpleInfo')
    Response getProjectSimpleInfo(@QueryParam("id") Long id){
        MyResponse.ok {
            TSProject project = TSProject.get(id)
            if(project){
                return [
                        director:project.director,
                        supervisor:project.supervisor,
                        stockStructure:project.stockStructure,
                        debt:project.debt,
                        assets:project.assets,
                        pdesc:project.pdesc,
                        endSummary:project.endSummary
                ]
            }else{
                return "{}"
            }

        }
    }

    @POST
    @Path('/updateProjectSimpleInfo')
    Response updateProjectSimpleInfo(TSProject projectInfo,@QueryParam("id") Long id){
        MyResponse.ok {
            TSProject project = TSProject.get(id)
            if(project&&projectInfo){
                    project.director=projectInfo.director
                    project.supervisor=projectInfo.supervisor
                    project.stockStructure=projectInfo.stockStructure
                    project.debt=projectInfo.debt
                    project.assets=projectInfo.assets
                    project.pdesc=projectInfo.pdesc
                    project.endSummary=projectInfo.endSummary
            }
        }
    }

    @GET
    @Path('/getProjectStockRight')
    Response getProjectStockRight(@QueryParam("id") Long id){
        MyResponse.ok {
            TSProject project = TSProject.get(id)
            if(project){
                return project.stockRights
            }else{
                return "{}"
            }

        }
    }

    @POST
    @Path('/addProjectStockRight')
    Response addProjectStockRight(StockRight stockRight,@QueryParam("id") Long id){
        MyResponse.ok {
            TSProject project = TSProject.get(id)
            if(project&&stockRight){
                def obj=stockRight.save(failOnError: true)
                project.addToStockRights(obj)
                return obj
            }else{
                return "{}"
            }
        }
    }

    @DELETE
    @Path('/delProjectStockRight')
    Response delProjectStockRight(@QueryParam("id") Long id,@QueryParam("stock_right_id") Long stockRightId){
        MyResponse.ok {
            TSProject project = TSProject.get(id)
            if(project){
                project.removeFromStockRights(StockRight.get(stockRightId))
            }else{
                return "{}"
            }
        }
    }

    @GET
    @Path('/getProjectFiles')
    Response getProjectFile(@QueryParam("id") Long id,@QueryParam("type") String type){
        MyResponse.ok {
            TSProject project = TSProject.get(id)
            if(project){
                if(type=="project"){
                    return project.startProjectFiles
                }else{
                    return project.endProjectFiles
                }
            }else{
                return "{}"
            }

        }
    }

    @POST
    @Path('/addProjectFile')
    Response addProjectFile(UploadFile uploadFile,@QueryParam("id") Long id,@QueryParam("type") String type){
        MyResponse.ok {
            TSProject project = TSProject.get(id)
            if(project&&uploadFile){
                def obj=uploadFile.save(failOnError: true)
                if(type=="project"){
                    project.addToStartProjectFiles(obj)
                }else{
                    project.addToEndProjectFiles(obj)
                }
                return obj
            }else{
                return "{}"
            }
        }
    }

    @DELETE
    @Path('/delProjectFile')
    Response delProjectFile(@QueryParam("id") Long id,@QueryParam("file_id") Long fileId,@QueryParam("type") String type){
        MyResponse.ok {
            TSProject project = TSProject.get(id)
            if(project){
                def file=UploadFile.get(fileId)
                if(type=="project"){
                    project.removeFromStartProjectFiles(file)
                }else{
                    project.removeFromEndProjectFiles(file)
                }
            }else{
                return "{}"
            }
        }
    }

    @DELETE
    @Path('/delProjectFile2')
    Response delProjectFile2(@QueryParam("file_id") String fileId){
        MyResponse.ok {

            def file=UploadFile.findByFilePath(fileId)
            def adminRole = Role.findByAuthority('ROLE_ADMIN')
            if(file?.creator){
                User current = springSecurityService.getCurrentUser()
                if(file.creator == current){
                    file?.delete()
                }else if(current.authorities.contains(adminRole)){
                    file?.delete()
                }else{
                    throw new Exception("未授权")
                }
            }

        }
    }

}
















