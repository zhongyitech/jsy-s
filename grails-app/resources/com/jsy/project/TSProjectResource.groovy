package com.jsy.project

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
class TSProjectResource {

    def TSProjectResourceService
    def id

    @GET
    Response read() {
        ok TSProjectResourceService.read(id)
    }

    @PUT
    Response update(TSProject dto) {
        dto.id = id
        ok TSProjectResourceService.update(dto)
    }

    @DELETE
    void delete() {
        TSProjectResourceService.delete(id)
    }
}
