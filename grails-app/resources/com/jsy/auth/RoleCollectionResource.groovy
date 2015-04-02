package com.jsy.auth

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.DELETE
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam

import static com.jsy.utility.MyResponse.*
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

        ok {
            return roleResourceService.create(dto)
        }

//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def dd
//        try {
//            dd=roleResourceService.create(dto)
//            result.put("rest_status", restStatus)
//            result.put("rest_result", dd as JSON)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//            result.put("rest_status", restStatus)
//            result.put("rest_result", dd as JSON)
//            return Response.ok(result.toString()).status(500).build()
//        }
    }

    @GET
    Response readAll() {
        ok {
            def ia = roleResourceService.readAll()
            ia
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def ia
//        try {
//            ia = roleResourceService.readAll()
//            result.put("rest_status", restStatus)
//            result.put("rest_result", ia as JSON)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        } catch (Exception e) {
//            restStatus = REST_STATUS_FAI
//            print(e)
//            e.printStackTrace()
//            result.put("rest_status", restStatus)
//            result.put("rest_result", ia as JSON)
//            return Response.ok(result.toString()).status(500).build()
//        }
    }

//    @GET
//    @Path('/manager')
//    Response findManager(){
//
//    }
    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {

        page {
            print arg
            def result = roleResourceService.readAllForPage(arg)
            result
        }
//        print("Role.readAllForPage()")
//        int total
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def ra
//        def r
//        try {
//            ra=roleResourceService.readAllForPage(arg)
//            total = ra.get("size")
//            print(total)
//            r = ra.get("page")
//            result.put("rest_status", restStatus)
//            result.put("rest_result", r as JSON)
//            result.put("rest_total", total)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//            e.printStackTrace()
//            result.put("rest_status", restStatus)
//            result.put("rest_result", r)
//            result.put("rest_total", total)
//            return Response.ok(result.toString()).status(500).build()
//        }
    }

    @Path('/{id}')
    RoleResource getResource(@PathParam('id') Long id) {
        new RoleResource(roleResourceService: roleResourceService, id: id)
    }

    @PUT
    Response update(Role dto, @QueryParam('id') int id) {

        ok {
            dto.id = id
            def map = [:]
            map.name = dto.name
            return roleResourceService.update(dto, map)
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def dd
//        try {
//            dto.id=id
//            def map = [:]
//            map.name=dto.name
//            dd=roleResourceService.update(dto,map)
//            result.put("rest_status", restStatus)
//            result.put("rest_result", dd as JSON)
//
//
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//            result.put("rest_status", restStatus)
//            result.put("rest_result", dd as JSON)
//            return Response.ok(result.toString()).status(500).build()
//        }
    }

    @DELETE
    Response delete(@QueryParam('id') Long id) {
        ok {
            return roleResourceService.delete(id)
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def dd
//        try {
//
//            dd=roleResourceService.delete(id)
//            result.put("rest_status", restStatus)
//            result.put("rest_result", dd as JSON)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//            e.printStackTrace()
//            result.put("rest_status", restStatus)
//            result.put("rest_result", dd as JSON)
//            return Response.ok(result.toString()).status(500).build()
//        }
    }

    /**
     * 获取用户的角色列表
     * @param uid
     * @return
     */
    @GET
    @Path("/userRoleList")
    Response getReoleListForUser(@QueryParam("id") Long uid) {

        ok {
            def user = User.get(uid)
            if(user==null){
                throw  new Exception("User not Found !")
            }
            return UserRole.getRoleListForUser(user)
        }

//        try {
//            def user = User.get(uid)
//            if (user != null) {
//                def rolelist = UserRole.getRoleListForUser(user)
//                ok JsonResult.success(rolelist)
//            } else {
//                ok JsonResult.error("没有此用户ID")
//            }
//        }
//        catch (Exception e) {
//            ok JsonResult.error(e.message)
//        }
    }

    @Path("/getResourceRole")
    Response getResourceRole(@QueryParam('id') Long id) {

        ok{
            return ResourceRole.findAllByRole(Role.get(id))
        }
//
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def dd
//        try {
//            dd = ResourceRole.findAllByRole(Role.get(id))
//            result.put("rest_status", restStatus)
//            result.put("rest_result", dd as JSON)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        } catch (Exception e) {
//            restStatus = REST_STATUS_FAI
//            print(e)
//            e.printStackTrace()
//            result.put("rest_status", restStatus)
//            result.put("rest_result", dd as JSON)
//            return Response.ok(result.toString()).status(500).build()
//        }

    }

    @GET
    @Path('/nameLike')
    Response findByNameLike(@QueryParam('params') String username) {
        ok {
            def roles = Role.findAllByNameLike("%" + username + "%")
            def jsonArray = []
            roles.each {
                def jso = [:]
                jso.put("value", it.name)
                jso.put("data", it.id)
                jsonArray.add(jso)
            }
            def data = [:]
            data.put("query", "Unit")
            data.put("suggestions", jsonArray)

            return data
        }
    }

}
