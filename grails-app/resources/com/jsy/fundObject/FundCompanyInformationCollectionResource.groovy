package com.jsy.fundObject

import com.jsy.system.TypeConfig
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.json.JSONArray

import javax.ws.rs.DELETE
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam


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
            result.put("rest_status", restStatus)
            result.put("rest_result", bfpr as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", bfpr as JSON)
            return Response.ok(result.toString()).status(500).build()

        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", bfpr as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

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

    @DELETE
    @Path('/delete')
    Response delete(@QueryParam('id') Long id){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def  rc
        try {
            rc = fundCompanyInformationResourceService.delete(id)
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
        org.json.JSONObject result = new org.json.JSONObject();
        String restStatus = REST_STATUS_SUC;
        int total
        org.json.JSONObject json
        JSONArray fp
        try {
            json = fundCompanyInformationResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            total = json.get("size")
            fp = json.get("page")
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", fp.toString())
        result.put("rest_total", total)

        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }


    @GET
    @Path("/readAll")
    Response readAll() {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ja = new JSONArray()
        try {
            def page = fundCompanyInformationResourceService.readAll()
            page.each {
                JSONObject jsonObject =new JSONObject((it as JSON).toString());
                def pars = new JSONArray()
                it?.hhrpx?.split(",").each {fid->
                    pars.put(FundCompanyInformation.get(Long.valueOf(fid)) as JSON)
                }
                jsonObject.put("partner", pars)
                ja.put(jsonObject)
            }

            result.put("rest_status", restStatus)
            result.put("rest_result", ja.toString())
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", ja.toString())
            return Response.ok(result.toString()).status(500).build()
        }
    }

    @GET
    @Path('/listAll')
    Response listAll() {
        ok fundCompanyInformationResourceService.readAll()
    }

    @GET
    @Path("/findById")
    Response findById(@QueryParam('id') Long id) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def bfpr
        try {
            bfpr = FundCompanyInformation.get(id)
            result.put("rest_status", restStatus)
            result.put("rest_result", bfpr as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", bfpr as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
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


    @GET
    @Path('/findByType')
    Response findByType(@QueryParam('type') Long type){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def dp
        try{
            dp = FundCompanyInformation.findAllByCompanyType(TypeConfig.get(type))
            result.put("rest_status", restStatus)
            result.put("rest_result", dp as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
            result.put("rest_status", restStatus)
            result.put("rest_result", dp as JSON)
            return Response.ok(result.toString()).status(500).build()
        }


    }

    @GET
    @Path('/getAccountFromFundName')
    Response getAccountFromFundName(@QueryParam('fundName') String fundName){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def dp
        try{
            dp = FundCompanyInformation.findAllByFund(Fund.findByFundName(fundName)).bankAccount
//            dp = FundCompanyInformation.findAllByCompanyType(TypeConfig.get(type))
            result.put("rest_status", restStatus)
            result.put("rest_result", dp as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", dp as JSON)
            return Response.ok(result.toString()).status(500).build()
        }


    }




}
