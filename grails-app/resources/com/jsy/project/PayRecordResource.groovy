package com.jsy.project

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.PUT
import javax.ws.rs.core.Response

@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class PayRecordResource {

    def payRecordResourceService
    def id

    @GET
    Response read() {
        ok payRecordResourceService.read(id)
    }

    @PUT
    Response update(PayRecord dto) {
        dto.id = id
        ok payRecordResourceService.update(dto)
    }

    @DELETE
    void delete() {
        payRecordResourceService.delete(id)
    }
}
