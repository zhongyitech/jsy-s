package com.jsy.system

import com.jsy.auth.User
import com.jsy.auth.UserRole
import com.jsy.fundObject.Finfo
import grails.converters.JSON
import org.json.JSONObject

import javax.ws.rs.QueryParam

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/toDoTask')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class ToDoTaskCollectionResource {

    def toDoTaskResourceService
    def springSecurityService

    @POST
    Response create(ToDoTask dto) {
        created toDoTaskResourceService.create(dto)
    }

    //查询所有待办任务
    @POST
    @Path('/getTodo')
    Response readAll(Finfo finfo) {
        User user=springSecurityService.getCurrentUser()
        //取用户所有的角色
        def roles=UserRole.findAllByUser(user).collect {it.role}
        //返回的任务为未完成状态的
        def toDoTasks=ToDoTask.findAllByCljsInListAndStatus(roles,0,[max: finfo.pagesize,sort:"cjsj", order:"asc", offset:finfo.startposition])
        int total=ToDoTask.findAllByCljsInListAndStatus(roles,0).size()
        JSONObject result = new JSONObject();
        result.put("rest_status", "suc")
        result.put("rest_result", toDoTasks as JSON)
        result.put("rest_total", total)
        return Response.ok(result.toString()).status(200).build()
    }

    //查询待办任务个数
    @GET
    @Path('/getTotal')
    Response readAllTotal() {
        User user=springSecurityService.getCurrentUser()
        def roles=UserRole.findAllByUser(user).collect {it.role}
        //返回的任务为未完成状态的
        def toDoTasks=ToDoTask.findAllByCljsInListAndStatus(roles,0)
        ok toDoTasks.size()
    }


    @GET
    @Path('/getById')
    Response getTodo(@QueryParam('id') Long id) {
        JSONObject result = new JSONObject();
        result.put("rest_status", 200)
        result.put("rest_result", ToDoTask.get(id) as JSON)
        return Response.ok(result.toString()).status(200).build()

//        new ToDoTaskResource(toDoTaskResourceService: toDoTaskResourceService, id: id)
    }

    @Path('/{id}')
    ToDoTaskResource getResource(@PathParam('id') Long id) {
        new ToDoTaskResource(toDoTaskResourceService: toDoTaskResourceService, id: id)
    }
}
