package com.jsy.fundObject

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

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

@Path('/api/yieldRange')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class YieldRangeCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"

    def yieldRangeResourceService
//   *****************
    //收益率范围
//   *****************
    @POST
    Response create(YieldRange dto) {
        created yieldRangeResourceService.create(dto)
    }

    @GET
    Response readAll() {
        ok yieldRangeResourceService.readAll()
    }

    @Path('/{id}')
    YieldRangeResource getResource(@PathParam('id') Long id) {
        new YieldRangeResource(yieldRangeResourceService: yieldRangeResourceService, id: id)
    }

    @GET
    @Path('/getyieldrangebyid')
    Response getyieldrangebyid(@QueryParam('id') Long id){
        ok {
            def yr = YieldRange.get(id)
            yr

        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def yr
//        try {
//            yr = YieldRange.get(id)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", yr as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }
}
