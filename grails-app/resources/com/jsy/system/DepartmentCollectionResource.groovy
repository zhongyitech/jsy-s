package com.jsy.system

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.QueryParam

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/department')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class DepartmentCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def departmentResourceService

//    JSONObject result = new JSONObject();
//    String restStatus = REST_STATUS_SUC;
//    def **
//    try{
//        ** = departmentResourceService.readAll()
//    }catch (Exception e){
//        restStatus = REST_STATUS_FAI
//        print(e)
//    }
//    result.put("rest_status", restStatus)
//    result.put("rest_result", dp as JSON)
//    return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    @POST
    Response create(Department dto) {
        created departmentResourceService.create(dto)
    }

    @GET
    Response readAll() {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def dp
        try{
            dp = departmentResourceService.readAll()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", dp as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    @Path('/findByName')
    Response findByName(@QueryParam('parm') String parm){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def dp
        try{
            dp = Department.findAllByDeptNameLike(parm)

        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }

        result.put("rest_status", restStatus)
        result.put("rest_result", dp as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }


    @Path('/{id}')
    DepartmentResource getResource(@PathParam('id') Long id) {
        new DepartmentResource(departmentResourceService: departmentResourceService, id: id)
    }
}
