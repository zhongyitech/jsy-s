package com.jsy.auth

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
class ResourceRoleResource {

    def resourceRoleResourceService
    def id

    @GET
    Response read() {
        ok resourceRoleResourceService.read(id)
    }

    @PUT
    Response update(ResourceRole dto) {
        dto.id = id
        ok resourceRoleResourceService.update(dto)
    }

    @DELETE
    void delete() {
        resourceRoleResourceService.delete(id)
    }
}
