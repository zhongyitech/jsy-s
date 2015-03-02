package com.jsy.auth

import com.jsy.fundObject.Finfo
import com.jsy.system.Department
import com.jsy.system.DepartmentCollectionResource
import com.jsy.system.DepartmentResourceService
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.json.JSONArray

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

@Path('/api/user')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class UserCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def userResourceService
    def springSecurityService

    //创建用户
    @POST
    Response create(User dto) {
        print("UserCollectionResource.create()")
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def dd
        try {
            dd = userResourceService.create(dto)
            print(dd.properties)
            result.put("rest_status", restStatus)
            result.put("rest_result", dd as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            e.printStackTrace()
            print(e)
            result.put("rest_status", restStatus)
            result.put("rest_result", dd as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", dd as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    @Path('/getUser')
    Response getUser() {
        ok (springSecurityService.getCurrentUser())
    }

    //删除用户
    @DELETE
    @Path('/{id}')
    Response delete(@PathParam('id') Long id) {
        boolean result=userResourceService.delete(id)
        print(result)
        ok('{result:'+result+'}')
    }
    //更改用户信息
//    @PUT
//    @Path('/{id}')
//    Response update(User dto,@PathParam('id') Long id) {
//        ok userResourceService.update(dto,id)
//    }

    //查询所有用户
    @GET
    Response readAll() {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ia
        try {
            ia = userResourceService.readAll()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ia as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    @Path('/nameLike')
    Response findByNameLike(@QueryParam('params') String username){
        def users=User.findAllByUsernameLikeOrChainNameLike("%"+username+"%","%"+username+"%")
        JSONArray jsonArray=new JSONArray()
        users.each {
            JSONObject jso=new JSONObject()
            jso.put("value",it.chainName)
            jso.put("data",it.id)
            jsonArray.put(jso)
        }
        JSONObject jsonObject=new JSONObject()
        jsonObject.put("query","Unit")
        jsonObject.put("suggestions",jsonArray)

        ok jsonObject.toString()
    }


    @GET
    @Path('/username({username})')
    Response findByName(@PathParam('username') String username){
        ok userResourceService.findByName(username)
    }

    @Path('/{id}')
    UserResource getResource(@PathParam('id') Long id) {
        new UserResource(userResourceService: userResourceService, id:id)
    }

    @GET
    @Path('/findUserFromRole')
    Response findUserFromRole(@QueryParam('authority') String authority){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def users
        try{
            if (null == authority || "" == authority){
                users = userResourceService.readAll()
            }else {
                Role role=Role.findByAuthority(authority)
                users=UserRole.findAllByRole(role).collect{it.user}
            }

        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }

        result.put("rest_status", restStatus)
        result.put("rest_result", users as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }
    @GET
    @Path('/findUserFromRoleAndDepartment')
    Response findUserFromRoleAndDepartment(@QueryParam('authority') String authority,@QueryParam('departmentid') String departmentid){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def users
        try{

            Role role=Role.findByAuthority(authority)
            users=UserRole.findAllByRole(role).collect{it.user}
            if([] == users){
                print("找不到该用户！")
            }
            print("users = " + users)
            def dp = Department.get(departmentid)
            print("dp.id = " + dp.id)
            print(users.department.id)
            def user = User.findByDepartment( dp)
            users = user.find(users)
            print(users)

        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }

        result.put("rest_status", restStatus)
        result.put("rest_result", users as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }
    @GET
    @Path('/findUser')
    Response findUser(@QueryParam('uid') String uid){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def users
        try{

            users = User.get(uid)

        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }

        result.put("rest_status", restStatus)
        result.put("rest_result", users as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @PUT
    Response update(User dto,@QueryParam('id') Long id) {
        print("id = "+id)
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def dd
        try {
//            dto.id=id
//            print("dto.id = "+dto.id)
            dd=userResourceService.update(dto,id)
            result.put("rest_status", restStatus)
            result.put("rest_result", dd as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", dd as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", dd as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Finfo finfo){
        print("userCollectionResource.readAllForPage()")
        org.json.JSONObject result = new org.json.JSONObject();
        String restStatus = REST_STATUS_SUC;
        int total
        def ia
        try {
            org.json.JSONObject json = userResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            total = json.get("size")
            print(total)
            ia = json.get("page")
            print(ia)
            result.put("rest_status", restStatus)
            result.put("rest_result", ia as JSON)
            result.put("rest_total", total)

            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", ia as JSON)
            result.put("rest_total", total)

            return Response.ok(result.toString()).status(500).build()
        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", ia as JSON)
//        result.put("rest_total", total)
//
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @POST
    @Path('/updatePassword')
    Response updatePassword(User dto){
        print("userCollectionResource.updatePassword()")
        org.json.JSONObject result = new org.json.JSONObject();
        String restStatus = REST_STATUS_SUC;
        try {
            User obj = springSecurityService.getCurrentUser()
            obj.password = dto.password
            obj.save(failOnError: true)

            result.put("rest_status", restStatus)
            result.put("rest_result", obj as JSON)

            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            e.printStackTrace()
            print(e)
            result.put("rest_status", restStatus)

            return Response.ok(result.toString()).status(500).build()
        }


    }

}
