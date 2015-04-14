package com.jsy.bankConfig

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/bankOrder')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class BankOrderCollectionResource {

    def bankOrderResourceService

    @POST
    Response create(BankOrder dto) {
        created bankOrderResourceService.create(dto)
    }

    @GET
    Response readAll() {
        ok bankOrderResourceService.readAll()
    }

    @Path('/{id}')
    BankOrderResource getResource(@PathParam('id') Long id) {
        new BankOrderResource(bankOrderResourceService: bankOrderResourceService, id: id)
    }
}
