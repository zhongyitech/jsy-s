package com.jsy.project

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/payRecord')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class PayRecordCollectionResource {

    def payRecordResourceService

    @POST
    @Path('/create')
    Response create(PayRecord dto) {
//        created payRecordResourceService.create(dto)


        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";

        try{

            payRecordResourceService.create(dto)

            result.put("rest_status", restStatus)
            result.put("rest_result", dto as JSON)
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            print(e)
            result.put("rest_status", restStatus)
            result.put("rest_result", dto as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
    }

    @GET
    Response readAll() {
        ok payRecordResourceService.readAll()
    }

    @Path('/{id}')
    PayRecordResource getResource(@PathParam('id') Long id) {
        new PayRecordResource(payRecordResourceService: payRecordResourceService, id:id)
    }
}
