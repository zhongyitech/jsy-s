package com.jsy.archives

import grails.converters.JSON
import org.json.JSONObject

import javax.ws.rs.QueryParam

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/userCommision')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class UserCommisionCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def userCommisionResourceService

    @POST
    Response create(UserCommision dto) {
        created userCommisionResourceService.create(dto)
    }

    @GET
    Response getbyid(@QueryParam('id') Long id) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def uc
        try {
            uc = UserCommision.get(id)
            print(uc)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)

        }
        result.put("rest_status", restStatus)
        result.put("rest_result", uc as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @Path('/{id}')
    UserCommisionResource getResource(@PathParam('id') Long id) {
        new UserCommisionResource(userCommisionResourceService: userCommisionResourceService, id: id)
    }
}
