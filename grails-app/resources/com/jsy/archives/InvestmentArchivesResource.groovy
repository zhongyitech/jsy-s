package com.jsy.archives

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.PUT
import javax.ws.rs.core.Response

import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class InvestmentArchivesResource {

    def investmentArchivesResourceService
    def id

    @GET
    Response read() {
        ok investmentArchivesResourceService.read(id)
    }

    @PUT
    Response update(InvestmentArchives dto) {
        dto.id = id
        ok investmentArchivesResourceService.update(dto)
    }

    @DELETE
    void delete() {
        investmentArchivesResourceService.delete(id)
    }
}
