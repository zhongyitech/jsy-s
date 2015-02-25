package com.jsy.project

import com.jsy.auth.Role
import com.jsy.auth.User
import com.jsy.fundObject.Finfo
import grails.converters.JSON

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/TSWorkflow')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class TSWorkflowCollectionResource {

    def workflowResourceService


    @GET
    @Path('/irData')
    Response initData() {
        init_project();
        init_flowModel();
        init_flow();

        ok "good"
    }


    def init_project={
        def admin = User.findByUsername('admin') ?: new User(
                username: 'admin',
                password: 'admin',
                department:department,
                chainName: "张三",
                enabled: true).save(failOnError: true)

        (1..22).each { i ->
            TSProject.findByName('project'+i) ?: new TSProject(
                    name: 'project'+i,
                    projectOwner: admin,
                    creator: admin,
                    creatorName: admin.chainName,
                    fundNames:"fund1,fund2,fund3"
            ).save(failOnError: true)

        }
    }

    def init_flow={
        TSProject.findAll().each {proj->
            workflowResourceService.createFlow(proj.id)
        }

    }



    def init_flowModel = {
        workflowResourceService.initFlowModel()
    }


}



























