package com.jsy.bankConfig

import grails.converters.JSON
import org.json.JSONObject

import javax.ws.rs.PUT
import javax.ws.rs.QueryParam

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/fundToBank')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class FundToBankCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def fundToBankResourceService

    @POST
    Response create(FundToBank dto) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ftb
        try {
            ftb = fundToBankResourceService.create(dto)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ftb as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    Response readAll() {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ftb
        try {
            ftb = fundToBankResourceService.readAll()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ftb as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @PUT
    Response update(FundToBank dto,@QueryParam('id') Long id){
        print(dto.properties)
        print(id)
        dto.id = id
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ftb
        try {
            ftb = fundToBankResourceService.update(dto)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ftb as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @Path('/{id}')
    FundToBankResource getResource(@PathParam('id') Long id) {
        new FundToBankResource(fundToBankResourceService: fundToBankResourceService, id:id)
    }
}
