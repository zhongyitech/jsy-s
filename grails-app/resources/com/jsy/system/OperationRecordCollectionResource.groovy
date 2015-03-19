package com.jsy.system

import com.jsy.fundObject.Finfo
import com.jsy.utility.JsonResult
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
    Response readAllForPage(Map arg) {
        print("operationRecordResourceService.readAllForPage()")
        try {
            def result = operationRecordResourceService.readAllForPage(arg.pagesize, arg.startposition, arg.query)
            ok JsonResult.success(result.data,result.total)
        }catch (Exception e){
            print(e)
            ok JsonResult.error(e.message)
        }
    }
}
