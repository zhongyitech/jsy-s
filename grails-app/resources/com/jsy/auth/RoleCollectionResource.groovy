package com.jsy.auth

import com.jsy.fundObject.Finfo
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.DELETE
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

@Path('/api/role')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class RoleCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def roleResourceService

    @POST
    Response create(Role dto) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def dd
        try {
            dd=roleResourceService.create(dto)
            result.put("rest_status", restStatus)
            result.put("rest_result", dd as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
            result.put("rest_status", restStatus)
            result.put("rest_result", dd as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
    }

    @GET
    Response readAll() {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ia
        try {
            ia = roleResourceService.readAll()
            result.put("rest_status", restStatus)
            result.put("rest_result", ia as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", ia as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
    }

//    @GET
//    @Path('/manager')
//    Response findManager(){
//
//    }
    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Finfo finfo) {
        print("Role.readAllForPage()")
        int total
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ra
        def r
        try {
            ra=roleResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            total = ra.get("size")
            print(total)
            r = ra.get("page")
            result.put("rest_status", restStatus)
            result.put("rest_result", r as JSON)
            result.put("rest_total", total)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", r)
            result.put("rest_total", total)
            return Response.ok(result.toString()).status(500).build()
        }
    }

    @Path('/{id}')
    RoleResource getResource(@PathParam('id') Long id) {
        new RoleResource(roleResourceService: roleResourceService, id: id)
    }

    @PUT
    Response update(Role dto,@QueryParam('id') String id) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def dd
        try {
            dto.id=id
            dd=roleResourceService.update(dto)
            result.put("rest_status", restStatus)
            result.put("rest_result", dd as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
            result.put("rest_status", restStatus)
            result.put("rest_result", dd as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
    }

    @DELETE
    Response delete(@QueryParam('id') Long id){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def dd
        try {

            dd=roleResourceService.delete(id)
            result.put("rest_status", restStatus)
            result.put("rest_result", dd as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", dd as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
    }

    @GET
    @Path("/getResourceRole")
    Response getResourceRole(@QueryParam('id') Long id){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def dd
        try {
            dd = ResourceRole.findAllByRole(Role.get(id))
            result.put("rest_status", restStatus)
            result.put("rest_result", dd as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", dd as JSON)
            return Response.ok(result.toString()).status(500).build()
        }

    }

}
