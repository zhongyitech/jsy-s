package com.jsy.archives

import com.jsy.fundObject.Finfo
import com.jsy.utility.DomainHelper
import grails.converters.JSON
import org.json.JSONObject

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

@Path('/api/filePackage')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class FilePackageCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"

    FilePackageResourceService filePackageResourceService

    /**
     * 档案入库操作
     * @param dto
     * @return
     */
    @POST
    Response create(FilePackage dto) {
        ok {
            def result=filePackageResourceService.create(dto)
            return result
        }
    }

//    @GET
//    @Path('/readAllForPage')
//    Response readAllForPage(def f) {
//        print("filePackageResourceService.readAllForPage()")
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        int total
//        JSONObject json
//        def fp
//        try {
//            json = filePackageResourceService.readAllForPage(f.pagesize, f.startposition, f.queryparam)
//            total = json.get("size")
//            print(total)
//            fp = json.get("page")
//            print(fp)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", fp as JSON)
//        result.put("rest_total", total)
//
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//
//    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        page {
            def dc = DomainHelper.getDetachedCriteria(FilePackage, arg)
            //todo: other code

            //按分页要求返回数据格式 [数据,总页数]
            return [data: dc.list([max: arg.pagesize, offset: arg.startposition]), total: arg.startposition == 0 ? dc.count():0]
        }
//        print("filePackageResourceService.readAllForPage()")
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        int total
//        JSONObject json
//        def fp
//        try {
//            json = filePackageResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
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



    @PUT
    Response update(FilePackage dto,@QueryParam('id') Long id){
        ok {
            def fp = filePackageResourceService.update(dto)
            fp
        }
//        print(dto.id)
//        print(dto)
//        print(id)
//        dto.id = id
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        JSONObject json
//        def fp
//        try {
//            fp = filePackageResourceService.update(dto)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", fp as JSON)
//
//
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @GET
    Response readAll() {
        ok filePackageResourceService.readAll()
    }

    @Path('/{id}')
    FilePackageResource getResource(@PathParam('id') Long id) {
        new FilePackageResource(filePackageResourceService: filePackageResourceService, id:id)
    }
}
