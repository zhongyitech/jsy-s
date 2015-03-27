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
class MenusRoleResource {

    def menusRoleResourceService
    def id

    @GET
    Response read() {
        ok menusRoleResourceService.read(id)
    }

    @PUT
    Response update(MenusRole dto) {
        dto.id = id
        ok menusRoleResourceService.update(dto)
    }

    @DELETE
    void delete() {
        menusRoleResourceService.delete(id)
    }
}
