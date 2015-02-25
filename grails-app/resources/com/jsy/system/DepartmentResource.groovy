package com.jsy.system

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
class DepartmentResource {

    def departmentResourceService
    def id

    @GET
    Response read() {
        ok departmentResourceService.read(id)
    }

    @PUT
    Response update(Department dto) {
        dto.id = id
        ok departmentResourceService.update(dto)
    }

    @DELETE
    void delete() {
        departmentResourceService.delete(id)
    }
}
