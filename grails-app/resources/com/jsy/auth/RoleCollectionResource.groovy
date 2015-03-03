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
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", dd as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @DELETE
    @Path('/delete')
    Response delete(@QueryParam('id') Long id){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def  rc
        try {
            rc = roleResourceService.delete(id)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", rc as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @GET
    @Path("/readAll")
    Response readAll() {
        JSONObject result = new JSONObject()
        int status = 500
        try {
            def all = roleResourceService.readAll()
            result.put("rest_result", all as JSON)
            status = 200
        }catch (Exception e){
            result.put("rest_status", "error")
            e.printStackTrace()
        }
        return Response.ok(result.toString()).status(status).build()
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Finfo finfo) {
        print("fundCompanyInformationResourceService.readAllForPage()")
        org.json.JSONObject result = new org.json.JSONObject();
        String restStatus = REST_STATUS_SUC;
        int total
        org.json.JSONObject json
        def fp
        try {
            json = roleResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            total = json.get("size")
            print(total)
            fp = json.get("page")
            print(fp)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", fp as JSON)
        result.put("rest_total", total)

        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

//    @GET
//    @Path('/manager')
//    Response findManager(){
//
//    }

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
//            dto.id=id
            dd=roleResourceService.update(dto, id)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            e.printStackTrace()
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", dd as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

}
