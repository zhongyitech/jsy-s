package com.jsy.flow

import com.jsy.archives.InvestmentArchives
import com.jsy.auth.User
import com.jsy.utility.ContractFlow
import com.jsy.utility.InvestmentFlow
import com.jsy.utility.SpecialFlow
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.json.JSONObject

//import static org.grails.jaxrs.response.Responses.*
import static com.jsy.utility.MyResponse.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/wtfksq')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class WtfksqCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def springSecurityService
    WtfksqResourceService wtfksqResourceService

    @POST
    @Path('/create')
    Response create(Wtfksq dto) {
        ok {
            dto.sqr = springSecurityService.getCurrentUser()
            dto.sqbm = dto.sqr.department ? dto.sqr.department.deptName : ""
            def iv=InvestmentArchives.findByContractNum(dto.htbh)
            SpecialFlow.Create.Validation(iv)
            def wd = wtfksqResourceService.create(dto)
            wd
        }
    }

    @GET
    Response readAll() {
        ok wtfksqResourceService.readAll()
    }

    @Path('/{id}')
    WtfksqResource getResource(@PathParam('id') Long id) {
        new WtfksqResource(wtfksqResourceService: wtfksqResourceService, id: id)
    }


}
