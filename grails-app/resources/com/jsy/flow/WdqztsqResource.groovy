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
class WdqztsqResource {

    def wdqztsqResourceService
    def id

    @GET
    Response read() {
        ok wdqztsqResourceService.read(id)
    }

    @PUT
    Response update(Wdqztsq dto) {
        dto.id = id
        ok wdqztsqResourceService.update(dto)
    }

    @DELETE
    void delete() {
        wdqztsqResourceService.delete(id)
    }
}
