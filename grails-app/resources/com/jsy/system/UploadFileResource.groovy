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
class UploadFileResource {

    def uploadFileResourceService
    def id

    @GET
    Response read() {
        ok uploadFileResourceService.read(id)
    }

    @PUT
    Response update(UploadFile dto) {
        dto.id = id
        ok uploadFileResourceService.update(dto)
    }

    @DELETE
    void delete() {
        uploadFileResourceService.delete(id)
    }
}
