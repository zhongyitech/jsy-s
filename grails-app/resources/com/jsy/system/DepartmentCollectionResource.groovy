package com.jsy.system

import com.jsy.fundObject.Finfo
import com.jsy.utility.JsonResult
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
    @GET
    @Path('/readAll')
    Response readAll(){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def bfpr
        try {
            bfpr = Department.findAll()
            result.put("rest_status", restStatus)
            result.put("rest_result", bfpr as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", bfpr as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
    }
    /**
     * 创建部门信息
     * @param dto
     * @return
     */
    @PUT
    Response create(Department dto) {
        def bfpr
        try {
            bfpr = departmentResourceService.create(dto)
                ok JsonResult.success(bfpr)
        }catch (Exception e){
            e.printStackTrace()
            ok JsonResult.error(e.message)
        }
    }

    /**
     * 更新部门的信息
     * @param dto
     * @param id
     * @return
     */
    @POST
    Response update(Department dto,@QueryParam('id') Long id){
        dto.id = id
        def  rc
        try {
            rc = departmentResourceService.update(dto)
            ok JsonResult.success(rc)
        }catch (Exception e){
            print(e)
            ok JsonResult.error(e.message)
        }
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Finfo finfo) {
        org.json.JSONObject result = new org.json.JSONObject();
        String restStatus = REST_STATUS_SUC;
        int total
        org.json.JSONObject json
        def fp
        try {
            json = departmentResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            total = json.get("size")
            print(total)
            fp = json.get("page")
            print(fp)
            result.put("rest_status", restStatus)
            result.put("rest_result", fp as JSON)
            result.put("rest_total", total)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", fp as JSON)
            result.put("rest_total", total)
            return Response.ok(result.toString()).status(500).build()
        }
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
    @DELETE
    Response delete(@QueryParam('id') Long id){
        def dp
        try{
            dp = departmentResourceService.delete(id)
            ok JsonResult.success(dp)
        }catch (Exception e){
            print(e)
            ok JsonResult.error(e.message)
        }
    }

    @Path('/{id}')
    DepartmentResource getResource(@PathParam('id') Long id) {
        new DepartmentResource(departmentResourceService: departmentResourceService, id: id)
    }
}
