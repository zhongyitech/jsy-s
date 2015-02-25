package com.jsy.archives

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
class FilePackageResource {

    def filePackageResourceService
    def id

    @GET
    Response read() {
        ok filePackageResourceService.read(id)
    }

    @PUT
    Response update(FilePackage dto) {
        dto.id = id
        ok filePackageResourceService.update(dto)
    }

    @DELETE
    void delete() {
        filePackageResourceService.delete(id)
    }
}
