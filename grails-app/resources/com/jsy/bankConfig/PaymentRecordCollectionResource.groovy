package com.jsy.bankConfig

import com.jsy.fundObject.Finfo
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

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

@Path('/api/paymentRecord')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class PaymentRecordCollectionResource {

    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"

    def paymentRecordResourceService

    @POST
    Response create(PaymentRecord dto) {

        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def bfpr
        try {
            bfpr = paymentRecordResourceService.create(dto)

        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)

        }
        result.put("rest_status", restStatus)
        result.put("rest_result", bfpr as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @PUT
    Response update(PaymentRecord dto,@QueryParam('id') Long id){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        dto.id = id
        def  rc
        try {
            rc = paymentRecordResourceService.update(dto)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", rc as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Finfo finfo) {
        org.json.JSONObject result = new org.json.JSONObject();
        String restStatus = REST_STATUS_SUC;
        int total
        org.json.JSONObject json
        def fp
        try {
            json = paymentRecordResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            total = json.get("size")
            fp = json.get("page")
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", fp as JSON)
        result.put("rest_total", total)

        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @PUT
    @Path('/updateStatus')
    Response updateStatus(@QueryParam('id') Long id,@QueryParam('type') int type){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def pr
        try {
            pr = PaymentRecord.get(id)
            pr.dealStatus = type
            pr.save(failOnError: true)

        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", pr as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    Response readAll() {
        ok paymentRecordResourceService.readAll()
    }

    @Path('/{id}')
    PaymentRecordResource getResource(@PathParam('id') Long id) {
        new PaymentRecordResource(paymentRecordResourceService: paymentRecordResourceService, id: id)
    }
}
