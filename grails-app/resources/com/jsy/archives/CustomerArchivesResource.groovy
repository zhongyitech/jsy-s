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
class CustomerArchivesResource {

    def customerArchivesResourceService
    def id

    @GET
    Response read() {
        ok customerArchivesResourceService.read(id)
    }

    @PUT
    Response update(CustomerArchives dto) {
        dto.id = id
        ok customerArchivesResourceService.update(dto)
    }

    @DELETE
    void delete() {
        customerArchivesResourceService.delete(id)
    }
}
