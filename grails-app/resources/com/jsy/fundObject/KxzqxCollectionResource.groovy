package com.jsy.fundObject

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.QueryParam

//import static org.grails.jaxrs.response.Responses.*
import static com.jsy.utility.MyResponse.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/kxzqx')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class KxzqxCollectionResource {

    def kxzqxResourceService

    @POST
    Response create(Kxzqx dto) {
        created kxzqxResourceService.create(dto)
    }

    @GET
    Response readAll() {
        ok kxzqxResourceService.readAll()
    }
    @GET
    @Path('/getById')
    Response getResource(@QueryParam('id') Long id) {
        ok {
            return  Kxzqx.get(id)
        }
//        JSONObject result = new JSONObject();
//        result.put("rest_status", 200)
//        result.put("rest_result", Kxzqx.get(id) as JSON)
//        return Response.ok(result.toString()).status(200).build()
    }
}
