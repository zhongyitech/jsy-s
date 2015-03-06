package com.jsy.project

import com.jsy.auth.Role
import com.jsy.auth.User
import com.jsy.fundObject.Finfo
import com.jsy.fundObject.Fund
import com.jsy.system.TypeConfig
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

    WorkflowResourceService workflowResourceService


    @GET
    @Path('/irData')
    Response initData() {
        init_fund();
        init_company();
        init_project();
        init_flowModel();
        init_flow();


        ok "good"
    }

    def init_fund={
        workflowResourceService.initFund()
    }


    def init_project={

        workflowResourceService.createProjects()
    }

    def init_flow={
        TSProject.findAll().each {proj->
            workflowResourceService.createFlow(proj.id)
        }
    }



    def init_flowModel = {
        workflowResourceService.initFlowModel()
    }

    def init_company = {
        workflowResourceService.initCompany()
    }




}



























