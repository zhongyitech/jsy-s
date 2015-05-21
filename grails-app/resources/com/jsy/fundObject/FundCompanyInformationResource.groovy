
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
class FundCompanyInformationResource {

    def fundCompanyInformationResourceService
    def id

    @GET
    Response read() {
        ok fundCompanyInformationResourceService.read(id)
    }

    @PUT
    Response update(FundCompanyInformation dto) {
        dto.id = id
        ok fundCompanyInformationResourceService.update(dto)
    }

    @DELETE
    void delete() {
        fundCompanyInformationResourceService.delete(id)
    }
}
