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
class CompanyResource {

    def companyResourceService
    def id

    @GET
    Response read() {
        ok companyResourceService.read(id)
    }

    @PUT
    Response update(Company dto) {
        dto.id = id
        ok companyResourceService.update(dto)
    }

    @DELETE
    void delete() {
        companyResourceService.delete(id)
    }
}
