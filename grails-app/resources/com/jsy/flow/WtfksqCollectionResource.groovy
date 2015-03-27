package com.jsy.flow

import grails.converters.JSON
import org.json.JSONObject

//import static org.grails.jaxrs.response.Responses.*
import static com.jsy.utility.MyResponse.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/wtfksq')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class WtfksqCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"

    def wtfksqResourceService

    @POST
    Response create(Wtfksq dto) {
        ok {
            def  wd =  wtfksqResourceService.create(dto)
            wd
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def wd
//        try {
//            wd =  wtfksqResourceService.create(dto)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", wd as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    Response readAll() {
        ok wtfksqResourceService.readAll()
    }

    @Path('/{id}')
    WtfksqResource getResource(@PathParam('id') Long id) {
        new WtfksqResource(wtfksqResourceService: wtfksqResourceService, id: id)
    }



}
