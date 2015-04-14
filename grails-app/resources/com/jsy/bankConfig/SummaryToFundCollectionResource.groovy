package com.jsy.bankConfig

import com.jsy.utility.DomainHelper

import javax.ws.rs.DELETE
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

@Path('/api/summaryToFund')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class SummaryToFundCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def summaryToFundResourceService



    @POST
    Response create(SummaryToFund dto) {
        ok {
            def ftb = summaryToFundResourceService.create(dto)
            ftb
        }
    }

    @GET
    Response readAll() {
        ok {
            def ftb = summaryToFundResourceService.readAll()
            ftb
        }
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        page {
            DomainHelper.getPage(SummaryToFund, arg)
        }
    }

    @PUT
    Response update(SummaryToFund dto,@QueryParam('id') Long id){
        ok {
            dto.id = id
            def ftb = summaryToFundResourceService.update(dto)
            ftb
        }
    }


    @Path('/{id}')
    SummaryToFundResource getResource(@PathParam('id') Long id) {
        new SummaryToFundResource(summaryToFundResourceService: summaryToFundResourceService, id: id)
    }

    @DELETE
    Response del(@QueryParam('id') Long id) {
        ok {
            summaryToFundResourceService.delete(id)
        }
    }
}
