package com.jsy.flow

import com.jsy.archives.InvestmentArchives
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

@Path('/api/jjxtsq')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class JjxtsqCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def jjxtsqResourceService

    @POST
    Response create(Jjxtsq dto) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def jj
        try {
            jj = jjxtsqResourceService.create(dto)
            InvestmentArchives investmentArchives=InvestmentArchives.get(dto.oldArchivesId)
            investmentArchives.dazt=3
            investmentArchives.status=2
            investmentArchives.save(failOnError: true)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", jj as JSON)

        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    Response getById(@QueryParam('id') Long id) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def wd
        try {
            wd =  Jjxtsq.findById(id)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", wd as JSON)

        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }
    @PUT
    Response update(Jjxtsq dto,@QueryParam('id') Long id){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        dto.id = id
        def wd
        try {
            wd =  jjxtsqResourceService.update(dto)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", wd as JSON)

        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @Path('/{id}')
    JjxtsqResource getResource(@PathParam('id') Long id) {
        new JjxtsqResource(jjxtsqResourceService: jjxtsqResourceService, id: id)
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Finfo finfo) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        int total
        def ia
        try {
            JSONObject json = jjxtsqResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            total = json.get("size")
            ia = json.get("page")
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ia as JSON)
        result.put("rest_total", total)

        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

}
