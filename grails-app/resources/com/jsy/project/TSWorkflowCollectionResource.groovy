package com.jsy.project

import GsonTools.GsonTool
import Models.MsgModel
import com.jsy.archives.CustomerArchives
import com.jsy.archives.InvestmentArchives
import com.jsy.archives.PaymentInfo
import com.jsy.auth.Role
import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Finfo
import com.jsy.fundObject.Fund
import com.jsy.fundObject.Kxzqx
import com.jsy.system.TypeConfig
import grails.converters.JSON
import sun.misc.resources.Messages_sv

import javax.ws.rs.QueryParam

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
    TestService testService

    @GET
    @Path('/irData')
    Response initData() {
        workflowResourceService.initData();
        ok "good"
    }

    @GET
    @Path('/irData2')
    Response initData2() {
        return Response.ok(testService.testTransation()).status(200).build()
    }

    @GET
    @Path('/setFundCompanyInformation')
    Response setFundCompanyInformation(){
        workflowResourceService.initFundCompanyInformation();

        ok "init FundCompanyInformation ok";
    }

    @GET
    @Path('/initUser')
     Response initUser(){

    }
}



























