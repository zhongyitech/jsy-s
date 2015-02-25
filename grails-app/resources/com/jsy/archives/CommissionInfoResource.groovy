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
class CommissionInfoResource {

    def commissionInfoResourceService
    def id

    @GET
    Response read() {
        ok commissionInfoResourceService.read(id)
    }

    @PUT
    Response update(CommissionInfo dto) {
        dto.id = id
        ok commissionInfoResourceService.update(dto)
    }

    @DELETE
    void delete() {
        commissionInfoResourceService.delete(id)
    }
}
