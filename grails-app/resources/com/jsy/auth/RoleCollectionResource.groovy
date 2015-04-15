package com.jsy.auth

import com.jsy.fundObject.Fund
import com.jsy.utility.DomainHelper
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
            if(Role.findByName(dto.name)){
                throw  new Exception("角色名称重复了!")
            }
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
    }
    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {

        page {
            def result = DomainHelper.getPage(Role,arg)
            result
        }
    }

    @Path('/{id}')
    RoleResource getResource(@PathParam('id') Long id) {
        new RoleResource(roleResourceService: roleResourceService, id: id)
    }

    @PUT
    Response update(Role dto, @QueryParam('id') int id) {

        ok {
            def role=Role.get(id);
            if(role.name==dto.name){
                return  true;
            }
            if(Role.findByName(dto.name)){
                throw  new Exception("角色名称重复了!")
            }
            dto.id = id
            def map = [:]
            map.name = dto.name
            return roleResourceService.update(dto, map)
        }
    }

    @DELETE
    Response delete(@QueryParam('id') Long id) {
        ok {
            return roleResourceService.delete(id)
        }
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

    }

    @Path("/getResourceRole")
    Response getResourceRole(@QueryParam('id') Long id) {

        ok{
            return ResourceRole.findAllByRole(Role.get(id))
        }

    }

    @GET
    @Path('/nameLike')
    Response findByNameLike(@QueryParam('params') String username) {
        def roles = Role.findAllByNameLikeOrAuthorityLike("%" + username + "%","%" + username + "%")
        org.json.JSONArray jsonArray = new org.json.JSONArray()
        roles.each {
            JSONObject jso = new JSONObject()
            jso.put("value", it.name)
            jso.put("data", it.id)
            jsonArray.put(jso)
        }
        JSONObject jsonObject = new JSONObject()
        jsonObject.put("query", "Unit")
        jsonObject.put("suggestions", jsonArray)

        return Response.ok(jsonObject.toString()).status(RESPONSE_STATUS_SUC).build();
    }
}
