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
class BorrowFilesPackageRecordsResource {

    def borrowFilesPackageRecordsResourceService
    def id

    @GET
    Response read() {
        ok borrowFilesPackageRecordsResourceService.read(id)
    }

    @PUT
    Response update(BorrowFilesPackageRecords dto) {
        dto.id = id
        ok borrowFilesPackageRecordsResourceService.update(dto)
    }

    @DELETE
    void delete() {
        borrowFilesPackageRecordsResourceService.delete(id)
    }
}
