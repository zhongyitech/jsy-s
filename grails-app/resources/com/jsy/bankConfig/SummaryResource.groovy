package com.jsy.bankConfig

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.PUT
import javax.ws.rs.core.Response

import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class SummaryResource {

    def summaryResourceService
    def id

    @GET
    Response read() {
        ok summaryResourceService.read(id)
    }

    @PUT
    Response update(Summary dto) {
        dto.id = id
        ok summaryResourceService.update(dto)
    }

    @DELETE
    void delete() {
        summaryResourceService.delete(id)
    }
}
