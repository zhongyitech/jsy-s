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
class PaymentInfoResource {

    def paymentInfoResourceService
    def id

    @GET
    Response read() {
        ok paymentInfoResourceService.read(id)
    }

    @PUT
    Response update(PaymentInfo dto) {
        dto.id = id
        ok paymentInfoResourceService.update(dto)
    }

    @DELETE
    void delete() {
        paymentInfoResourceService.delete(id)
    }
}
