package com.jsy.archives

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/customerArchives')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class CustomerArchivesCollectionResource {

    def customerArchivesResourceService

    @POST
    Response create(CustomerArchives dto) {
        created customerArchivesResourceService.create(dto)
    }

    @GET
    Response readAll() {
        ok customerArchivesResourceService.readAll()
    }

    @Path('/{id}')
    CustomerArchivesResource getResource(@PathParam('id') Long id) {
        new CustomerArchivesResource(customerArchivesResourceService: customerArchivesResourceService, id: id)
    }
}
