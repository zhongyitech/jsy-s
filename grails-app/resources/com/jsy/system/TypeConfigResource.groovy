package com.jsy.system

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
class TypeConfigResource {

    def typeConfigResourceService
    def id

    @GET
    Response read() {
        ok typeConfigResourceService.read(id)
    }

    @PUT
    Response update(TypeConfig dto) {
        dto.id = id
        ok typeConfigResourceService.update(dto)
    }

    @DELETE
    void delete() {
        typeConfigResourceService.delete(id)
    }
}
