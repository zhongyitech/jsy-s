package com.jsy.system

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/company')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class CompanyCollectionResource {

    def companyResourceService

    @POST
    Response create(Company dto) {
        created companyResourceService.create(dto)
    }

    @GET
    @Path('/listAll')
    Response readAll() {
        ok companyResourceService.readAll()
    }


    @Path('/{id}')
    CompanyResource getResource(@PathParam('id') Long id) {
        new CompanyResource(companyResourceService: companyResourceService, id:id)
    }
}
