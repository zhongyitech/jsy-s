package com.jsy.project

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
class ReceiveRecordResource {

    def receiveRecordResourceService
    def id

    @GET
    Response read() {
        ok receiveRecordResourceService.read(id)
    }

    @PUT
    Response update(ReceiveRecord dto) {
        dto.id = id
        ok receiveRecordResourceService.update(dto)
    }

    @DELETE
    void delete() {
        receiveRecordResourceService.delete(id)
    }
}
