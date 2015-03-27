package com.jsy.archives

import com.jsy.fundObject.Finfo
import com.jsy.utility.DomainHelper
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.DefaultValue
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

@Path('/api/borrowFilesPackageRecords')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class BorrowFilesPackageRecordsCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def borrowFilesPackageRecordsResourceService

    @POST
    Response create(BorrowFilesPackageRecords dto) {
        ok{
            BorrowFilesPackageRecords bfpr = borrowFilesPackageRecordsResourceService.create(dto)
            bfpr
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def bfpr
//        try {
//            bfpr = borrowFilesPackageRecordsResourceService.create(dto)
//
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            e.printStackTrace()
//
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", bfpr as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        page {
            def dc = DomainHelper.getDetachedCriteria(BorrowFilesPackageRecords, arg)
            //todo: other code

            //按分页要求返回数据格式 [数据,总页数]
            return [data: dc.list([max: arg.pagesize, offset: arg.startposition]), total: arg.startposition == 0 ? dc.count():0]
        }
//        org.json.JSONObject result = new org.json.JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        int total
//        org.json.JSONObject json
//        def fp
//        try {
//            json = borrowFilesPackageRecordsResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
//            total = json.get("size")
//            fp = json.get("page")
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", fp as JSON)
//        result.put("rest_total", total)
//
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @GET
    Response find(@QueryParam('fpid') String fpid){

        ok {
            def bfpr = BorrowFilesPackageRecords.get(fpid)
            bfpr
        }

//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def bfpr
//        try {
//            bfpr = BorrowFilesPackageRecords.get(fpid)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", bfpr as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    @Path('/findByFilepackageId')
    Response findByFilepackageId(@QueryParam('fpid') String fpid){

        ok {
            def bfpr
            def filePackage = FilePackage.get(fpid)
            def borrowFilePackge=BorrowFilesPackageRecords.findByFilePackage(filePackage);
            bfpr = borrowFilePackge.user.username
            bfpr
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def bfpr
//        try {
//            def filePackage = FilePackage.get(fpid)
//            if(!filePackage){
//                restStatus = REST_STATUS_FAI;
//            }else{
//                def borrowFilePackge=BorrowFilesPackageRecords.findByFilePackage(filePackage);
//                if(borrowFilePackge){
//                    bfpr = borrowFilePackge.user.username
//                }else{
//                    restStatus = REST_STATUS_FAI;
//                }
//            }
//
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", bfpr)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @PUT
    Response update(BorrowFilesPackageRecords dto){

        ok {
            def rc = borrowFilesPackageRecordsResourceService.update(dto)
            rc
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def  rc
//        try {
//            rc = borrowFilesPackageRecordsResourceService.update(dto)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", rc as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @Path('/{id}')
    BorrowFilesPackageRecordsResource getResource(@PathParam('id') Long id) {
        new BorrowFilesPackageRecordsResource(borrowFilesPackageRecordsResourceService: borrowFilesPackageRecordsResourceService, id: id)
    }
}
