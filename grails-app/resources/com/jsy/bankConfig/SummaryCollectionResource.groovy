package com.jsy.bankConfig

import com.jsy.utility.DomainHelper

import javax.ws.rs.DELETE
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam

//import static org.grails.jaxrs.response.Responses.*
import static com.jsy.utility.MyResponse.*


import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/summary')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class SummaryCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    SummaryResourceService summaryResourceService


    @POST
    Response create(Summary dto) {
        ok {

                dto.indexlocation = Summary.last() ==null ? 1: Summary.last().indexlocation + 1
            def ftb = summaryResourceService.create(dto)
            ftb
        }
    }

    @GET
    Response readAll() {
        ok {
            def ftb = summaryResourceService.readAll()
            ftb
        }
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        page {
            DomainHelper.getPage(Summary, arg)
        }
    }

    @DELETE
    Response del(@QueryParam('id') Long id) {
        ok {
            summaryResourceService.delete(id)
        }
    }

    @PUT
    Response update(Summary dto, @QueryParam('id') Long id) {
        ok {
            dto.id = id
            def ftb = summaryResourceService.update(dto)
            ftb
        }
    }


    @POST
    @Path('/up')
    Response up(@QueryParam('id') Long id) {
        ok {
            summaryResourceService.upIndex(id)
        }
    }

    @POST
    @Path('/down')
    Response down(@QueryParam('id') Long id) {
        ok {
            summaryResourceService.downIndex(id)
        }
    }

    @GET
    @Path('/selectList')
    Response selectList() {
        ok {
            Summary.list().collect {
                [id: it.id, mapName: it.summary]
            }
        }
    }

    @Path('/{id}')
    SummaryResource getResource(@PathParam('id') Long id) {
        new SummaryResource(summaryResourceService: summaryResourceService, id: id)
    }
}
