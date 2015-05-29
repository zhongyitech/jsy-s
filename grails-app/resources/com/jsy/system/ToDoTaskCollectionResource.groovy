package com.jsy.system

import com.jsy.auth.User
import com.jsy.auth.UserRole
import com.jsy.fundObject.Finfo
import com.jsy.utility.DomainHelper
import com.jsy.utility.MyResponse
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
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
    SpringSecurityService springSecurityService

    //查询所有待办任务
    @POST
    @Path('/all')
    Response readAll(Map arg) {
        MyResponse.page {
            DomainHelper.getPage(ToDoTask, arg)
        }
    }
    //查询所有待办任务
    @POST
    @Path('/getTodo')
    Response getTodo(Map arg) {
        MyResponse.page {
            def dc = DomainHelper.getDetachedCriteria(ToDoTask, arg)
            User user = springSecurityService.getCurrentUser()
            println(user.getAuthorities())
            dc = dc.inList("cljs", user.getAuthorities())

            return [data: dc.list([max: arg.pagesize, offset: arg.startposition]), total: dc.count()]
        }
    }

    //查询待办任务个数
    @GET
    @Path('/getTotal')
    Response readAllTotal() {
        MyResponse.ok {
            User user = springSecurityService.getCurrentUser()
            def roles = UserRole.findAllByUser(user).collect { it.role }
            //返回的任务为未完成状态的
            def toDoTasks = ToDoTask.findAllByCljsInListAndStatus(roles, 0)
            ok toDoTasks.size()
        }
    }


    @GET
    @Path('/getById')
    Response getTodo(@QueryParam('id') Long id) {
        MyResponse.ok {
            ToDoTask.get(id)
        }
    }

    @Path('/{id}')
    ToDoTaskResource getResource(@PathParam('id') Long id) {
        new ToDoTaskResource(toDoTaskResourceService: toDoTaskResourceService, id: id)
    }
}
