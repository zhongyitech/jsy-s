package com.jsy.flow

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
class ThclsqResource {

    def thclsqResourceService
    def id

    @GET
    Response read() {
        ok thclsqResourceService.read(id)
    }

    @PUT
    Response update(Thclsq dto) {
        dto.id = id
        ok thclsqResourceService.update(dto)
    }

    @DELETE
    void delete() {
        thclsqResourceService.delete(id)
    }
}
