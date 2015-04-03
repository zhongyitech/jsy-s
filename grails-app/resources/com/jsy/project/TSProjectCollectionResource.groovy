package com.jsy.project

import GsonTools.GsonTool
import Models.MsgModel
import com.jsy.fundObject.Finfo
import com.jsy.fundObject.Fund
import com.jsy.utility.CreateNumberService
import com.jsy.utility.JsonResult
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.PUT
import javax.ws.rs.QueryParam

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

        ok jsonObject.toString()
    }

    @GET
    @Path('/stepInfo')
    Response fetchStepInfo(@QueryParam('projectid') int projectid){
        JSONObject result = new JSONObject();
        String restStatus = "200";

        TSProject project=TSProject.get(projectid)
        if(!project){
            result.put("rest_status", 500)
            result.put("err","no.project.found")
            return Response.ok(result.toString()).status(500).build()
        }

        def allPhaseInfos = projectResourceService.getAllFlowPhaseInfo(project,springSecurityService.getCurrentUser());

        result.put("rest_status", 200)
        result.put("rest_result", allPhaseInfos as JSON)

        return Response.ok(result.toString()).status(200).build()
    }

    //提交收集
    @POST
    @Path('/complete_gather')
    Response completeGather(String datastr) {
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";

        // get project
        org.json.JSONObject obj = JSON.parse(datastr)
        def projectid = obj.projectid
        TSProject project = TSProject.get(projectid);
        if(!project){
            restStatus = "500";
            result.put("rest_status", restStatus)
            result.put("rest_result", "no such project found")
            return Response.ok(result.toString()).status(500).build()
        }

        try{
            //权限校验
            def user = springSecurityService.getCurrentUser();
            def phase = project.getProjectWorkflow().getGatherInfo()
            def accessable = projectResourceService.checkUserAccessable(phase,project,user);
            if(!accessable){
                restStatus = "500";
                result.put("rest_status", restStatus)
                result.put("rest_result", "you can not access this phase")
                return Response.ok(result.toString()).status(500).build()
            }



            //数据保存


            projectResourceService.completeGather(project,obj)

            result.put("rest_status", restStatus)
            result.put("rest_result", "")
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", e.getLocalizedMessage())
            return Response.ok(result.toString()).status(500).build()
        }
    }

    //提交现场考察
    @POST
    @Path('/complete_research')
    Response completeResearch(String datastr) {
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";

        // get project
        org.json.JSONObject obj = JSON.parse(datastr)
        def projectid = obj.projectid
        TSProject project = TSProject.get(projectid);
        if(!project){
            restStatus = "500";
            result.put("rest_status", restStatus)
            result.put("rest_result", "no such project found")
            return Response.ok(result.toString()).status(500).build()
        }

        try{
            //权限校验
            def user = springSecurityService.getCurrentUser();
            def phase = project.getProjectWorkflow().getResearch()
            def accessable = projectResourceService.checkUserAccessable(phase,project,user);
            if(!accessable){
                restStatus = "500";
                result.put("rest_status", restStatus)
                result.put("rest_result", "you can not access this phase")
                return Response.ok(result.toString()).status(500).build()
            }



            //数据保存
            projectResourceService.completeResearch(project,obj)

            result.put("rest_status", restStatus)
            result.put("rest_result", "")
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", e.getLocalizedMessage())
            return Response.ok(result.toString()).status(500).build()
        }
    }

    //提交会议
    @POST
    @Path('/complete_meeting')
    Response completeMeeting(String datastr) {
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";

        // get project
        org.json.JSONObject obj = JSON.parse(datastr)
        def projectid = obj.projectid
        TSProject project = TSProject.get(projectid);
        if(!project){
            restStatus = "500";
            result.put("rest_status", restStatus)
            result.put("rest_result", "no such project found")
            return Response.ok(result.toString()).status(500).build()
        }

        try{
            //权限校验
            def user = springSecurityService.getCurrentUser();
            def phase = project.getProjectWorkflow().getMeeting()
            def accessable = projectResourceService.checkUserAccessable(phase,project,user);
            if(!accessable){
                restStatus = "500";
                result.put("rest_status", restStatus)
                result.put("rest_result", "you can not access this phase")
                return Response.ok(result.toString()).status(500).build()
            }



            //数据保存
            projectResourceService.completeMeeting(project,obj)

            result.put("rest_status", restStatus)
            result.put("rest_result", "")
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", e.getLocalizedMessage())
            return Response.ok(result.toString()).status(500).build()
        }
    }


    //提交第三方法律
    @POST
    @Path('/complete_thirdpartyLow')
    Response completeThirdpartyLow(String datastr) {
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";

        // get project
        org.json.JSONObject obj = JSON.parse(datastr)
        def projectid = obj.projectid
        TSProject project = TSProject.get(projectid);
        if(!project){
            restStatus = "500";
            result.put("rest_status", restStatus)
            result.put("rest_result", "no such project found")
            return Response.ok(result.toString()).status(500).build()
        }

        try{
            //权限校验
            def user = springSecurityService.getCurrentUser();
            def phase = project.getProjectWorkflow().getOtherEA()
            def accessable = projectResourceService.checkUserAccessable(phase,project,user);
            if(!accessable){
                restStatus = "500";
                result.put("rest_status", restStatus)
                result.put("rest_result", "you can not access this phase")
                return Response.ok(result.toString()).status(500).build()
            }

            //数据保存
            projectResourceService.completeThirdpartyLow(project,obj)

            result.put("rest_status", restStatus)
            result.put("rest_result", "")
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", e.getLocalizedMessage())
            return Response.ok(result.toString()).status(500).build()
        }
    }

//    //提交添加合作伙伴
//    @POST
//    @Path('/complete_addCompany')
//    Response completeAddCompany(String datastr) {
//        JSONObject result = new JSONObject();
//        JSONArray table = new JSONArray();
//        String restStatus = "200";
//
//        // get project
//        org.json.JSONObject obj = JSON.parse(datastr)
//        def projectid = obj.projectid
//        TSProject project = TSProject.get(projectid);
//        if(!project){
//            restStatus = "500";
//            result.put("rest_status", restStatus)
//            result.put("rest_result", "no such project found")
//            return Response.ok(result.toString()).status(500).build()
//        }
//
//        try{
//            //权限校验
//            def user = springSecurityService.getCurrentUser();
//            def phase = project.getProjectWorkflow().getAddCompany()
//            def accessable = projectResourceService.checkUserAccessable(phase,project,user);
//            if(!accessable){
//                restStatus = "500";
//                result.put("rest_status", restStatus)
//                result.put("rest_result", "you can not access this phase")
//                return Response.ok(result.toString()).status(500).build()
//            }
//
//            //数据保存
//            projectResourceService.completeAddCompany(project,obj)
//
//            result.put("rest_status", restStatus)
//            result.put("rest_result", "")
//            return Response.ok(result.toString()).status(200).build()
//        }catch (Exception e){
//            restStatus = "500";
//            e.printStackTrace()
//            result.put("rest_status", restStatus)
//            result.put("rest_result", e.getLocalizedMessage())
//            return Response.ok(result.toString()).status(500).build()
//        }
//    }

    //提交添加合作伙伴
    @POST
    @Path('/complete_makeContact')
    Response completeMakeContact(String datastr) {
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";

        // get project
        org.json.JSONObject obj = JSON.parse(datastr)
        def projectid = obj.projectid
        TSProject project = TSProject.get(projectid);
        if(!project){
            restStatus = "500";
            result.put("rest_status", restStatus)
            result.put("rest_result", "no such project found")
            return Response.ok(result.toString()).status(500).build()
        }

        try{
            //权限校验
            def user = springSecurityService.getCurrentUser();
            def phase = project.getProjectWorkflow().getMakeContact()
            def accessable = projectResourceService.checkUserAccessable(phase,project,user);
            if(!accessable){
                restStatus = "500";
                result.put("rest_status", restStatus)
                result.put("rest_result", "you can not access this phase")
                return Response.ok(result.toString()).status(500).build()
            }

            //数据保存
            projectResourceService.completeMakeContact(project, obj)

            result.put("rest_status", restStatus)
            result.put("rest_result", "")
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", e.getLocalizedMessage())
            return Response.ok(result.toString()).status(500).build()
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


//

//            projectResourceService.completeGather(project,obj)

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
     * 获取工作流节点信息
     * @param projectId    项目ID
     * @param phaseIndex   节点Index
     * @return
     */
    @GET
    @Path('/getSpecailAccess')
    Response getSpecailAccess(@QueryParam("projectId") int projectId,@QueryParam("phaseIndex") int phaseIndex){
        MsgModel msgModel = projectResourceService.getSpecailAccess(projectId,phaseIndex);

        if(!msgModel){
            MsgModel msg = MsgModel.getErrorMsg("JAVA ERROR!");
            return Response.ok(GsonTool.getMsgModelJson(msg)).status(500).build();
        }

        if(msgModel.isSuccess()){
            return Response.ok(GsonTool.getMsgModelJson(msgModel)).status(200).build();
        }else{
            return Response.ok(GsonTool.getMsgModelJson(msgModel)).status(500).build();
        }
    }

    /**
     * 保存限时访问信息
     * @param specailAccesses
     * @return
     */
    @POST
    @Path('/setSpecailAccess')
    Response setSpecailAccess(SpecailAccess specailAccess){
        try{
            def msgModel = projectResourceService.setSpecailAccess(specailAccess);

            ok JsonResult.success(msgModel)
        }catch (Exception e){
            ok JsonResult.error(e.message)
        }


//
//        if(msgModel == null){
//            MsgModel msg = MsgModel.getErrorMsg("JAVA ERROR");
//            return Response.ok(GsonTool.getMsgModelJson(msg)).status(500).build();
//        }
//
//        if(msgModel.isSuccess()){
//            return Response.ok(GsonTool.getMsgModelJson(msgModel)).status(200).build()
//        }else{
//            return Response.ok(GsonTool.getMsgModelJson(msgModel)).status(500).build();
//        }


    }
}
















