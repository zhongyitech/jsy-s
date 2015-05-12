package com.jsy.auth

import com.jsy.bankServices.HEAD
import com.jsy.fundObject.Finfo
import com.jsy.system.Department
import com.jsy.system.TypeConfig
import com.jsy.utility.DomainHelper
import com.jsy.utility.JsonResult
import com.sun.jersey.json.impl.JSONHelper
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.json.JSONArray
import org.restlet.ext.json.JsonConverter

import javax.management.Query
import javax.ws.rs.DELETE
import javax.ws.rs.DefaultValue
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

@Path('/api/user')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class UserCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    UserResourceService userResourceService
    def springSecurityService
    AuthorityService authorityService

    //创建用户
    @PUT
    Response create(User dto, @QueryParam('rolelist') String rolelist) {

        ok {
            User user = userResourceService.create(dto)
            if (rolelist && rolelist != "") {
                rolelist.split(',').each {
                    UserRole.create(user, Role.get(Long.parseLong(it))).save(failOnError: true)
                }
            }
            user
        }
    }

    @GET
    @Path('/getUser')
    Response getUser() {
        ok {
            springSecurityService.getCurrentUser()
        }
    }

    //删除用户
    @DELETE
    @Path('/{id}')
    Response delete(@PathParam('id') Long id) {
        ok {
            return userResourceService.delete(id)
        }
    }


    @GET
    @Path('/nameLike')
    Response findByNameLike(@QueryParam('params') String username,
                            @QueryParam('extraData') String extraData,
                            @QueryParam('departmentId') @DefaultValue("-1") Long departmentId) {
        ok {
            def users = null
            if (departmentId > 0) {
                users = User.findAllByDepartmentAndUsernameLikeAndChainNameLike(departmentId, "%" + username + "%", "%" + username + "%")
            }
            if (users == null)
                users = User.findAllByUsernameLikeOrChainNameLike("%" + username + "%", "%" + username + "%")
            def jsonArray = []
            users.each {
                def jso = [:]
                jso.put("value", it.chainName + "(" + it.username + ")" + (it.department ? " | " + it.department.deptName : ""))
                jso.put("data", it.id)
                jsonArray.add(jso)
            }
            def data = [:]
            data.put("query", "Unit")
            data.put("suggestions", jsonArray)
            return data
        }
    }

    @GET
    @Path('/username({username})')
    Response findByName(@PathParam('username') String username) {
        ok {
            userResourceService.findByName(username)
        }
    }

    @Path('/{id}')
    UserResource getResource(@PathParam('id') Long id) {
        new UserResource(userResourceService: userResourceService, id: id)
    }

    /**
     * 获取职能为销售的部门列表
     * @return 部门列表
     */
    @GET
    @Path('/allDepartmentLader')
    Response allDepartmentLeader() {

        ok {
            def typeConfig = TypeConfig.findByTypeAndMapValue(8, 2)
            def dep = Department.findAllByPerformance(typeConfig);
            def result = []
            dep.each {
                if (it.leader != null)
                    result.add(it.leader);
            }
            return result
        }
    }

//    //替换为其它方法了
//    @GET
//    @Path('/findUserFromRoleAndDepartment')
//    Response findUserFromRoleAndDepartment(
//            @QueryParam('authority') String authority, @QueryParam('departmentid') String departmentid) {
//        def result;
//        def users
//        try {
//
//            Role role = Role.findByAuthority(authority)
//            users = UserRole.findAllByRole(role).collect { it.user }
//            if ([] == users) {
//                print("找不到该用户！")
//            }
//            print("users = " + users)
//            def dp = Department.get(departmentid)
//            print("dp.id = " + dp.id)
//            print(users.department.id)
//            def user = User.findByDepartment(dp)
//            users = user.find(users)
//            print(users)
//            result = JsonResult.success(user)
//
//        } catch (Exception e) {
//            print(e)
//            result = JsonResult.error(e.message)
//        }
////        result.put("rest_status", restStatus)
////        result.put("rest_result", users as JSON)
//        return Response.ok(result).status(RESPONSE_STATUS_SUC).build()
//    }

    /**
     * 获取指定的用户
     * @param uid
     * @return 返回用户对象
     */
    @GET
    @Path('/findUser')
    Response findUser(@QueryParam('uid') String uid) {

        ok {
            def users = User.get(uid)
            print(users.properties)
            def ur = UserRole.findAllByUser(users)
            int i = 0
            def role = [:]
            def userAndRole = [:]
            ur.each {
                role.put("" + i++ + "", Role.get(it.role.id))
            }
            userAndRole.put("user", users)
            userAndRole.put("role", role)

            return userAndRole
        }
    }

    /**
     * 获取指定用户的部门负责人
     * @param uid
     * @return User
     */
    @GET
    @Path('/findUserLeader')
    Response findUserLeader(@QueryParam('uid') Long uid) {

        ok {
            def users
            def leaders = [:]

            users = User.get(uid)
            if (users && users.department && users.department.leader) {
                leaders.id = users.department.leader.id
                leaders.chainName = users.department.leader.chainName
                leaders.username = users.department.leader.username
                return leaders
            } else {
                return null
            }
        }
    }

    /**
     * 获取指定用户的部门信息
     * @param id
     * @return
     */
    @GET
    @Path('/findUserDepartment')
    Response findUserDeparemnt(@QueryParam('uid') Long uid) {
        ok {
            def user = User.get(uid)
            user.department
        }
    }

    /**
     * 更新用户信息
     *
     * @param dto
     * @param id
     * @param rolelist
     * @return User 保存之后的用户信息
     */
    @POST
    Response update(User dto, @QueryParam('id') Long id, @QueryParam('rolelist') String rolelist) {

        //普通的返回方式
        ok {
            if (id == null || id == 0) {
                throw new Exception("id is null or 0")
            }

            def roles = new ArrayList<Long>()
            if (rolelist != null && rolelist != "") {
                rolelist.split(",").each {
                    roles.add(Long.parseLong(it))
                }
            }
            return [data: userResourceService.update(dto, id, roles)]
        }
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {

        //分页数据的调用方式
        page {
            def result = userResourceService.readAllForPage(arg)
            return result
        }
    }

    /**
     * 更新用户的密码，只对dto的密码字段进行更新。
     *
     * @param dto
     * @return User
     */
    @POST
    @Path('/updatePassword')
    Response updatePassword(User dto) {

        ok {
            User obj = springSecurityService.getCurrentUser()
            obj.password = dto.password
            obj.save(failOnError: true)
            return obj
        }
    }

    @GET
    @Path('getUsers')
    Response getUsers() {

        ok {
            User.findAll()
        }
    }

    /**
     *
     * @param name
     * @return
     */
    @GET
    @Path('/selectList')
    Response getSelectList(@QueryParam("name") String name) {
        ok {
            print(name)

            if (name != null && name.size() > 2) {
                return User.findAllByUsernameLikeOrChainNameLike("%" + name + "%", "%" + name + "%").collect {
                    [mapName: it.chainName, id: it.id]
                }
            } else {
                return User.list().collect {
                    [mapName: it.chainName, id: it.id]
                }
            }
        }
    }
}
