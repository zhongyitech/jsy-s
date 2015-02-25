package com.jsy.fundObject

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

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

@Path('/api/fundCompanyInformation')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class FundCompanyInformationCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def fundCompanyInformationResourceService

    @POST
    Response create(FundCompanyInformation dto) {

        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def bfpr
        try {
            bfpr = fundCompanyInformationResourceService.create(dto)

        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            e.printStackTrace()

        }
        result.put("rest_status", restStatus)
        result.put("rest_result", bfpr as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @PUT
    Response update(FundCompanyInformation dto,@QueryParam('id') Long id){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        dto.id = id
        def  rc
        try {
            rc = fundCompanyInformationResourceService.update(dto)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", rc as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Finfo finfo) {
        print("filePackageResourceService.readAllForPage()")
        org.json.JSONObject result = new org.json.JSONObject();
        String restStatus = REST_STATUS_SUC;
        int total
        org.json.JSONObject json
        def fp
        try {
            json = fundCompanyInformationResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
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


    @GET
    Response readAll() {
        ok fundCompanyInformationResourceService.readAll()
    }

    //获取募集基金账户信息
    @GET
    @Path('/getAccount')
    Response getAccount(@QueryParam('id') Long id){

        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def fc
        try {
//            fc = FundCompanyInformation.get(id).bankAccount
            fc = FundCompanyInformation.findByFundInList(Fund.get(id)).bankAccount
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            e.printStackTrace()
        print(e)
        }

    result.put("rest_status", restStatus)
    result.put("rest_result", fc as JSON)
    return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }


    @Path('/{id}')
    FundCompanyInformationResource getResource(@PathParam('id') Long id) {
        new FundCompanyInformationResource(fundCompanyInformationResourceService: fundCompanyInformationResourceService, id: id)
    }


}
