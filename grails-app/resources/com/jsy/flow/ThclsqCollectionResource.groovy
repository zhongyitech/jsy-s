package com.jsy.flow

import com.jsy.archives.InvestmentArchives
import com.jsy.fundObject.Finfo
import com.jsy.utility.DomainHelper
import grails.converters.JSON
import org.json.JSONObject

import javax.ws.rs.PUT
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

@Path('/api/thclsq')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class ThclsqCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"

    def thclsqResourceService

    @POST
    Response create(Thclsq dto) {
        ok {
            def th =  thclsqResourceService.create(dto)
            InvestmentArchives investmentArchives=InvestmentArchives.get(dto.oldArchivesId)
            investmentArchives.dazt=4
            investmentArchives.status=2
            investmentArchives.save(failOnError: true)
            th
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def th
//
//        try {
//            th =  thclsqResourceService.create(dto)
//            InvestmentArchives investmentArchives=InvestmentArchives.get(dto.oldArchivesId)
//            investmentArchives.dazt=4
//            investmentArchives.status=2
//            investmentArchives.save(failOnError: true)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", th as JSON)
//
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    Response getById(@QueryParam('id') Long id) {
        ok {
            def wd =  Thclsq.findById(id)
            wd
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def wd
//        try {
//            wd =  Thclsq.findById(id)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", wd as JSON)
//
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }
    @PUT
    Response update(Thclsq dto,@QueryParam('id') Long id){
        ok {
            dto.id = id
            def wd =  thclsqResourceService.update(dto)
            wd
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        dto.id = id
//        def wd
//        try {
//            wd =  thclsqResourceService.update(dto)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", wd as JSON)
//
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @Path('/{id}')
    ThclsqResource getResource(@PathParam('id') Long id) {
        new ThclsqResource(thclsqResourceService: thclsqResourceService, id: id)
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        page {
            def dc = DomainHelper.getDetachedCriteria(Thclsq, arg)
            //todo: other code

            //按分页要求返回数据格式 [数据,总页数]
            return [data: dc.list([max: arg.pagesize, offset: arg.startposition]), total: arg.startposition == 0 ? dc.count():0]
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        int total
//        def ia
//        try {
//            JSONObject json = thclsqResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
//            total = json.get("size")
//            ia = json.get("page")
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", ia as JSON)
//        result.put("rest_total", total)
//
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

}
