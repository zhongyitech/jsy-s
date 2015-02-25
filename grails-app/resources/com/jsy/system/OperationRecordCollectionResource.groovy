package com.jsy.system

import com.jsy.fundObject.Finfo
import grails.converters.JSON

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/operationRecord')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class OperationRecordCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def operationRecordResourceService

    @POST
    Response create(OperationRecord dto) {
        created operationRecordResourceService.create(dto)
    }

    @GET
    Response readAll() {
        ok operationRecordResourceService.readAll()
    }

    @Path('/{id}')
    OperationRecordResource getResource(@PathParam('id') Long id) {
        new OperationRecordResource(operationRecordResourceService: operationRecordResourceService, id: id)
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Finfo finfo) {
        print("operationRecordResourceService.readAllForPage()")
        org.json.JSONObject result = new org.json.JSONObject();
        String restStatus = REST_STATUS_SUC;
        int total
        org.json.JSONObject json
        def fp
        try {
            json = operationRecordResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            total = json.get("size")
            print(total)
            fp = json.get("page")
            print(fp)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", fp as JSON)
        result.put("rest_total", total)

        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }
}
