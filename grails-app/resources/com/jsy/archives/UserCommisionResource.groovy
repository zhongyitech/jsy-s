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
class UserCommisionResource {

    def userCommisionResourceService
    def id

    @GET
    Response read() {
        ok userCommisionResourceService.read(id)
    }

    @PUT
    Response update(UserCommision dto) {
        dto.id = id
        ok userCommisionResourceService.update(dto)
    }

    @DELETE
    void delete() {
        userCommisionResourceService.delete(id)
    }
}
