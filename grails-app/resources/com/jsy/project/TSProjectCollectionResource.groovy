package com.jsy.project

import com.jsy.fundObject.Finfo
import com.jsy.fundObject.Fund
import com.jsy.utility.CreateNumberService
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

    //新增
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
            def phase = projectResourceService.getPhase(project, "gatherInfo");
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


    @POST
    @Path('/delFile')
    Response delFile(@QueryParam('fileName') String fileName,@QueryParam('filePath') String filePath) {
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";


        try{
            //校验
            if(!fileName || "".equals(fileName) || !filePath || "".equals(filePath)){
                restStatus = "500";
                result.put("rest_status", restStatus)
                result.put("rest_result", "lack of fileName or filePath")
                return Response.ok(result.toString()).status(500).build()
            }




//            projectResourceService.completeGather(project,obj)

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
    @Path('/delTsFile')
    Response delTsFile(@QueryParam('id') int  id) {
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";


        try{


//            projectResourceService.completeGather(project,obj)

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

}
















