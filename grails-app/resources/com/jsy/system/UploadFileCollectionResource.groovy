package com.jsy.system

import grails.converters.JSON
import org.json.JSONObject

import javax.ws.rs.QueryParam

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/uploadFile')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class UploadFileCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"

    def uploadFileResourceService

    @POST
    Response create(UploadFile dto) {
        created uploadFileResourceService.create(dto)
    }

    @GET
    @Path("/getById")
    Response getById(@QueryParam('id') Long id) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def wd
        try {
            wd =  UploadFile.get(id)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", wd as JSON)

        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @Path('/{id}')
    UploadFileResource getResource(@PathParam('id') Long id) {
        new UploadFileResource(uploadFileResourceService: uploadFileResourceService, id: id)
    }



}
