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
        init_project();
        init_flowModel();
        init_flow();
        init_company();

        ok "good"
    }

    def init_fund={
        new Fund(fundName:'测试基金',fundNo:'F001',startSaleDate:new Date(),status:TypeConfig.findByTypeAndMapValue(1,2),owner:'张三',memo:'备注').save(failOnError: true)
        new Fund(fundName:'巨星基金',fundNo:'F002',startSaleDate:new Date(),status:TypeConfig.findByTypeAndMapValue(1,2),owner:'张三',memo:'备注').save(failOnError: true)
    }


    def init_project={
        def admin = User.findByUsername('admin') ?: new User(
                username: 'admin',
                password: 'admin',
                department:department,
                chainName: "张三",
                enabled: true).save(failOnError: true)

        (1..13).each { i ->
            TSProject.findByName('project'+i) ?: new TSProject(
                    name: 'project'+i,
                    projectDealer: 'dealer_'+i,
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

    def init_company = {
        workflowResourceService.initCompany()
    }




}



























