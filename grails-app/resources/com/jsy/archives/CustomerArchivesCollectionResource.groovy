package com.jsy.archives

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.json.JSONArray

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

@Path('/api/customerArchives')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class CustomerArchivesCollectionResource {

    def customerArchivesResourceService
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    //新建客户档案
    @POST
    Response create(CustomerArchives dto) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def custm
        try{
            custm = customerArchivesResourceService.create(dto)
            result.put("rest_status", restStatus)
            result.put("rest_result", custm as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
            result.put("rest_status", restStatus)
            result.put("rest_result", custm as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", custm as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        created customerArchivesResourceService.create(dto)
    }
 //查询全部客户档案
    @GET
    Response readAll() {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def custm
        try{
            custm = customerArchivesResourceService.readAll()
            result.put("rest_status", restStatus)
            result.put("rest_result", custm as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", custm as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
//        ok customerArchivesResourceService.readAll()
    }

    @Path('/{id}')
    CustomerArchivesResource getResource(@PathParam('id') Long id) {
        new CustomerArchivesResource(customerArchivesResourceService: customerArchivesResourceService, id: id)
    }

    //更新客户档案信息
    @PUT
    @Path('/update')
    Response update(CustomerArchives dto,@QueryParam('id') Long id){
        print("CustomerCollectionResources.update()")
        dto.id = id
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def custm
        try{
            custm = customerArchivesResourceService.update(dto)
            result.put("rest_status", restStatus)
            result.put("rest_result", custm as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
            result.put("rest_status", restStatus)
            result.put("rest_result", custm as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", custm as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        return "suc"
    }
    //根据客户姓名模糊查询客户档案
    @GET
    @Path('/nameLike')
    Response findByNameLike(@QueryParam('params') String username){
        def users=CustomerArchives.findAllByNameLike("%"+username+"%")
        JSONArray jsonArray=new JSONArray()
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
}
