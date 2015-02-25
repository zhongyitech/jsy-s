package com.jsy.bankConfig

import com.jsy.fundObject.Finfo
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

@Path('/api/bankTransactionsRecord')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class BankTransactionsRecordCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def bankTransactionsRecordResourceService

    @POST
    Response create(FundToBank dto) {
        print("create bankTransactionsRecordResourceService")
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ftb
        try {
            ftb = bankTransactionsRecordResourceService.create(dto)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ftb as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Finfo finfo) {
        print("bankTransactionsRecordResourceService .readAllForPage()")
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        int total
        def ia

        try {
            JSONObject json = bankTransactionsRecordResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            total = json.get("size")
            print(total)
            ia = json.get("page")
            print(ia)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ia as JSON)
        result.put("rest_total", total)

        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @PUT
    @Path('/dealing')
    Response dealing(@QueryParam('id') Long id,@QueryParam('type') Long type){
        print("dealing bankTransactionsRecordResourceService")
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ftb
        try {
            ftb = BankTransactionsRecord.get(id)
            ftb.managed = true
            ftb.manageType = type
            ftb.save(failOnError: true)

        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ftb as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @Path('/{id}')
    BankTransactionsRecordResource getResource(@PathParam('id') Long id) {
        new BankTransactionsRecordResource(bankTransactionsRecordResourceService: bankTransactionsRecordResourceService, id: id)
    }
}
