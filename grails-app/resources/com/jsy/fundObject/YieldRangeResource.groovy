package com.jsy.fundObject

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
class YieldRangeResource {

    def yieldRangeResourceService
    def id

    @GET
    Response read() {
        ok yieldRangeResourceService.read(id)
    }

    @PUT
    Response update(YieldRange dto) {
        dto.id = id
        ok yieldRangeResourceService.update(dto)
    }

    @DELETE
    void delete() {
        yieldRangeResourceService.delete(id)
    }
}
