package com.jsy.system

import com.jsy.utility.JsonResult
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.QueryParam

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/typeConfig')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class TypeConfigCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"

    def typeConfigResourceService
    SendMailService sendMailService

    @POST
    Response create(TypeConfig dto) {
        created typeConfigResourceService.create(dto)
    }

    @GET
    Response readAll() {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def tc
        try {
            tc = typeConfigResourceService.readAll()
            result.put("rest_status", restStatus)
            result.put("rest_result", tc as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        } catch (Exception e) {
            restStatus = REST_STATUS_FAI
            print(e.stackTrace)
            result.put("rest_status", restStatus)
            result.put("rest_result", tc as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
    }

    @Path('/{id}')
    TypeConfigResource getResource(@QueryParam('id') Long id) {
        new TypeConfigResource(typeConfigResourceService: typeConfigResourceService, id: id)
    }

    @GET
    @Path('/type')
    Response findByType(@QueryParam('type') int type) {
        def tc
        try {
            tc = typeConfigResourceService.findByType(type)
            ok JsonResult.success(tc)
        } catch (Exception e) {
            print(e.stackTrace)
            ok JsonResult.error(e.message)
        }
    }
}
