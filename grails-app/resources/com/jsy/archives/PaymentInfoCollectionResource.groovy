package com.jsy.archives

import com.jsy.fundObject.Finfo
import grails.converters.JSON
import org.json.JSONArray
import org.json.JSONObject

import javax.ws.rs.DefaultValue
import javax.ws.rs.QueryParam

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/paymentInfo')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class PaymentInfoCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"

    def paymentInfoResourceService

//    @POST
//    Response create(PaymentInfo dto) {
//        created paymentInfoResourceService.create(dto)
//    }

    @GET
    @Path('/toPay')
    Response toPay(@QueryParam('payIds') String payIds){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        JSONArray jsonArray=new JSONArray()
        payIds.split(",").each {
            JSONObject jbo=new JSONObject()
            boolean b=true
            Long payId=Long.valueOf(it)
            try{
                def dto=paymentInfoResourceService.toPay(payId)
            }catch (Exception e){
                b=false
            }
            jbo.put("id",payId)
            jbo.put("result",b)
            jsonArray.put(jbo)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", jsonArray.toString())
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @POST
    @Path("/readAllForPage")
    Response readAllForPage(Finfo finfo) {
        print("paymentInfoResourceService.readAllForPage()")
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        try{
            paymentInfoResourceService.addPaymentInfo()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
        }
        if (null == finfo.keyword){
            finfo.keyword = ""
        }
        def total
        def pi
        try {
            pi =    PaymentInfo.findAllByZfsjBetweenAndIsAllow(finfo.startsaledate1, finfo.startsaledate2, false,[max: finfo.pagesize,sort:"type", order:"asc", offset:finfo.startposition])
            total = PaymentInfo.findAllByZfsjBetweenAndIsAllow(finfo.startsaledate1, finfo.startsaledate2, false).size()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", pi as JSON)
        result.put("rest_total", total)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @Path('/{id}')
    PaymentInfoResource getResource(@PathParam('id') Long id) {
        new PaymentInfoResource(paymentInfoResourceService: paymentInfoResourceService, id: id)
    }
}
