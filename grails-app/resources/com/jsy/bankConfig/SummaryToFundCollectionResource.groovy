package com.jsy.bankConfig

import grails.converters.JSON
import org.json.JSONObject

import javax.ws.rs.PUT
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

@Path('/api/summaryToFund')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class SummaryToFundCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def summaryToFundResourceService

//    @POST
//    Response create(SummaryToFund dto) {
//        created summaryToFundResourceService.create(dto)
//    }
//
//    @GET
//    Response readAll() {
//        ok summaryToFundResourceService.readAll()
//    }

    @POST
    Response create(SummaryToFund dto) {
        ok {
            def ftb = summaryToFundResourceService.create(dto)
            ftb
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def ftb
//        try {
//            ftb = summaryToFundResourceService.create(dto)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", ftb as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    Response readAll() {
        ok {
            def ftb = summaryToFundResourceService.readAll()
            ftb
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def ftb
//        try {
//            ftb = summaryToFundResourceService.readAll()
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", ftb as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @PUT
    Response update(SummaryToFund dto,@QueryParam('id') Long id){
        ok {
            dto.id = id
            def ftb = summaryToFundResourceService.update(dto)
            ftb
        }
//        dto.id = id
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def ftb
//        try {
//            ftb = summaryToFundResourceService.update(dto)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", ftb as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }


    @Path('/{id}')
    SummaryToFundResource getResource(@PathParam('id') Long id) {
        new SummaryToFundResource(summaryToFundResourceService: summaryToFundResourceService, id: id)
    }
}
