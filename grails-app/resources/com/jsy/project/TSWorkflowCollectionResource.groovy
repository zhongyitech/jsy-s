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
        workflowResourceService.initData();
        ok "good"
    }

}



























