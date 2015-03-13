package com.jsy.archives

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jsy.fundObject.Finfo
import grails.converters.JSON
import org.json.JSONArray
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

@Path('/api/commissionInfo')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class CommissionInfoCollectionResource {

    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def commissionInfoResourceService

    @POST
    @Path('/addPayment')
    Response addPayment(CommissionInfo commissionInfo) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ci= commissionInfoResourceService.toPay(commissionInfo)
        result.put("rest_status", restStatus)
        result.put("rest_result", ci as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

//    @POST
//    Response create(CommissionInfo dto) {
//        created commissionInfoResourceService.create(dto)
//    }

    @GET
    Response readAll() {
        ok commissionInfoResourceService.readAll()
    }

    @GET
    @Path('/getByarchivesId')
    Response getByarchivesId(@QueryParam('archivesId') Long archivesId) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ci
        try {
            ci = CommissionInfo.findAllByArchivesId(archivesId)

        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)

        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ci as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @PUT
    @Path('/updateType')
    Response updateType(@QueryParam('id') Long id,@QueryParam('sl') double sl,@QueryParam('sqsh') boolean sqsh){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        CommissionInfo ci
        try {
//            ci = CommissionInfo.get(id)
            ci = commissionInfoResourceService.toPay(id)
            ci.sl = sl
            ci.sqsh = sqsh
            ci.save()

        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)

        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ci as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @POST
    @Path('/getcommissionInfo')
    Response getcommissionInfo(Finfo finfo) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ci
        def total
        try {
            ci = CommissionInfo.findAllByTypeNotEqualAndZfsjBetween(2,finfo.startsaledate1, finfo.startsaledate2,[max: finfo.pagesize,sort:"type", order:"asc", offset: finfo.startposition])
//            ci = CommissionInfo.listOrderByType()
            total = CommissionInfo.findAllByTypeNotEqualAndZfsjBetween(2,finfo.startsaledate1, finfo.startsaledate2).size()

        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)

        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ci as JSON)
        result.put("rest_total", total)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }


    @Path('/{id}')
    CommissionInfoResource getResource(@PathParam('id') Long id) {
        new CommissionInfoResource(commissionInfoResourceService: commissionInfoResourceService, id: id)
    }


    @GET
    @Path('/irData')
    Response initData() {
        commissionInfoResourceService.initData();
        ok "good"
    }
}
