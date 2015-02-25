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
class RegisterContractResource {

    def registerContractResourceService
    def id

    @GET
    Response read() {
        ok registerContractResourceService.read(id)
    }

    @PUT
    Response update(RegisterContract dto) {
        dto.id = id
        ok registerContractResourceService.update(dto)
    }

    @DELETE
    void delete() {
        registerContractResourceService.delete(id)
    }
}
