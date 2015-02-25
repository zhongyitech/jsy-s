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
class OperationRecordResource {

    def operationRecordResourceService
    def id

    @GET
    Response read() {
        ok operationRecordResourceService.read(id)
    }

    @PUT
    Response update(OperationRecord dto) {
        dto.id = id
        ok operationRecordResourceService.update(dto)
    }

    @DELETE
    void delete() {
        operationRecordResourceService.delete(id)
    }
}
