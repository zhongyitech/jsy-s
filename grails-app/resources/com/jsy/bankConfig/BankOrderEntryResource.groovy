package com.jsy.bankConfig

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
class BankOrderEntryResource {

    def bankOrderEntryResourceService
    def id

    @GET
    Response read() {
        ok bankOrderEntryResourceService.read(id)
    }

    @PUT
    Response update(BankOrderEntry dto) {
        dto.id = id
        ok bankOrderEntryResourceService.update(dto)
    }

    @DELETE
    void delete() {
        bankOrderEntryResourceService.delete(id)
    }
}
