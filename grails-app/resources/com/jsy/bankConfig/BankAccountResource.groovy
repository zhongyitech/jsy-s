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
class BankAccountResource {

    def bankAccountResourceService
    def id

    @GET
    Response read() {
        ok bankAccountResourceService.read(id)
    }

    @PUT
    Response update(BankAccount dto) {
        dto.id = id
        ok bankAccountResourceService.update(dto)
    }

    @DELETE
    void delete() {
        bankAccountResourceService.delete(id)
    }
}
