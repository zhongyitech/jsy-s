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
class WtfksqResource {

    def wtfksqResourceService
    def id

    @GET
    Response read() {
        ok wtfksqResourceService.read(id)
    }

    @PUT
    Response update(Wtfksq dto) {
        dto.id = id
        ok wtfksqResourceService.update(dto)
    }

    @DELETE
    void delete() {
        wtfksqResourceService.delete(id)
    }
}
