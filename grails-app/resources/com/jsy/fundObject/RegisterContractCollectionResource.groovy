package com.jsy.fundObject

import com.jsy.archives.Contract
import com.jsy.utility.CreateNumberService
import com.jsy.utility.DomainHelper
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.DELETE
import javax.ws.rs.DefaultValue
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam
import java.text.SimpleDateFormat

//import static org.grails.jaxrs.response.Responses.*
import static com.jsy.utility.MyResponse.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/registerContract')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class RegisterContractCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    public static final String REST_INFORMATION = "";
    def registerContractResourceService

    @POST
    Response create(RegisterContract dto) {
        ok {
            def rc
            StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("F"))
            dto.indexNum = CreateNumberService.getRandomNumber(new StringBuffer(former))
            rc=registerContractResourceService.create(dto)
            rc.indexNum = CreateNumberService.getFullNumber(former, dto.id.toString())
            rc.save(failOnError: true)
            rc
        }
//        //{"department":{"id":1},"receiveUser":{"id":1},"receiveDate":"2014-12-16T08:00:00Z","fund":{"id":1},"startNum":"123","endNum":"234","item-key":"0","total":111}
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def rc
//        try {
//            StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("F"))
////            StringBuffer funno = new StringBuffer("R")
////            Date d = new Date()
////            print(d)
////            dto.createDate = d
////            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd")
////            String da=sdf.format(d)
////            funno.append(da.toString())
////            int endnumber = Math.random()*10000 + Math.random()*1000 + Math.random()*100 + Math.random()*10
////            StringBuffer funnocut = new StringBuffer(funno)
////            print(funno)
////            funnocut.append(endnumber)
////            print(funno)
//            dto.indexNum = CreateNumberService.getRandomNumber(new StringBuffer(former))
//            rc=registerContractResourceService.create(dto)
////            print(rc.id)
////            print(funno)
////            funno.append(rc.id)
////            print(funno)
//            rc.indexNum = CreateNumberService.getFullNumber(former, dto.id.toString())
////            print(funno)
//            rc.save(failOnError: true)
////            rc = registerContractResourceService.create(dto)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        print(rc)
//        result.put("rest_status", restStatus)
//        result.put("rest_result", rc as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @DELETE
    Response delete(@PathParam('id') Long id){
        ok {

            return  registerContractResourceService.delete(id)
        }
    }

    @PUT
    Response update(RegisterContract dto,@QueryParam('id') Long id){
        ok {
            dto.id = id
            def rc = registerContractResourceService.update(dto)
            rc
        }
//        print(dto.id)
//        print(dto)
//        print(id)
//        dto.id = id
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def  rc
//        try {
//            rc = registerContractResourceService.update(dto)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", rc as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @GET
    Response readAll() {
        ok {
            def registerContract = registerContractResourceService.readAll()
            registerContract
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def registerContract
//        try {
//            registerContract = registerContractResourceService.readAll()
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", registerContract as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }
    @POST
    @Path('/readUseForPage')
    Response readUseForPage(Finfo finfo) {
        ok {
            def ret =[:]
            int total
            def rc
            JSONObject json
            json = registerContractResourceService.readUseForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            total = json.get("size")
            print(total)
            rc = json.get("page")
            ret.page = rc
            ret.total = total
            ret
        }
//        print("registerContract.readUseForPage")
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        int total
//        JSONObject json
//        def rc
//        try {
////            rc = registerContractResourceService.findByParm(queryparam)
////            total = rc.size()
//            json = registerContractResourceService.readUseForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
//            total = json.get("size")
//            print(total)
//            rc = json.get("page")
//            print(rc)
//
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", rc as JSON)
//        result.put("rest_total", total)
//
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @POST
    @Path('/readReturnForPage')
    Response readReturnForPage(Finfo finfo) {
        ok {
            def ret =[:]
            int total
            def rc
            JSONObject json
            json = registerContractResourceService.readReturnForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            total = json.get("size")
            print(total)
            rc = json.get("page")
            ret.page = rc
            ret.total = total
            ret
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        int total
//        JSONObject json
//        def rc
//        try {
////            rc = registerContractResourceService.findByParm(queryparam)
////            total = rc.size()
//            json = registerContractResourceService.readReturnForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
//            total = json.get("size")
//            print(total)
//            rc = json.get("page")
//            print(rc)
//
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", rc as JSON)
//        result.put("rest_total", total)
//
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @Path('/{id}')
    RegisterContractResource getResource(@PathParam('id') Long id) {
        new RegisterContractResource(registerContractResourceService: registerContractResourceService, id: id)
    }

    //领用
    @POST
    @Path('/useContract')
    Response useContract(RegisterContract dto){
        ok {
            dto.actionType = false
            def fund = dto.fund
            int sn = Integer.parseInt(dto.startNum.substring(5))
            int en = Integer.parseInt(dto.endNum.substring(5))
            JSONArray jc = new JSONArray()
            for (int i = sn;i <= en;i++){
                Contract c = Contract.findBySzbhAndSflyAndFund(i, false, fund)
                if (null == c){
                    break
                }else {
                    print("put "+i)
                    jc.put(c)
                }
            }
            if ("suc" == restStatus) {
                jc.each {
                    Contract d = it
                    d.sfly = true
                    d.save(failOnError: true)
                }

                StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("F"))
                dto.indexNum = CreateNumberService.getRandomNumber(new StringBuffer(former))
                def rc = registerContractResourceService.create(dto)
                rc.indexNum = CreateNumberService.getFullNumber(former, dto.id.toString())
                rc.save(failOnError: true)
                return rc
            }

        }
//        String rest_information = REST_INFORMATION
//        //{"indexNum":"0001","department":{"id":1}, "receiveUser":{"id":1}, "receiveDate":"2014-12-16T08:00:00Z", "fund":{"id":1}, "startNum":"CFYHN201", "endNum":"CFYHN250", "total":50}
////        {"department":{"id":1},"receiveUser":{"id":1},"receiveDate":"2014-12-16T08:00:00Z","fund":{"id":1},"startNum":"CFYHN201","endNum":"CFYHN250","total":50}
//        print("useContract RegisterContractResourceService")
//        dto.actionType = false
//        JSONObject result = new JSONObject();
//        def fund = dto.fund
//        String restStatus = REST_STATUS_SUC;
//        int sn = Integer.parseInt(dto.startNum.substring(5))
//        int en = Integer.parseInt(dto.endNum.substring(5))
//        JSONArray jc = new JSONArray()
//        for (int i = sn;i <= en;i++){
//            Contract c = Contract.findBySzbhAndSflyAndFund(i, false, fund)
//            if (null == c){
//                rest_information = "非法的合同编号！"
//                restStatus = "err"
//                print(restStatus)
//                print(rest_information)
//                break
//            }else {
//                print("put "+i)
//                jc.put(c)
//            }
//        }
//        if ("suc" == restStatus){
//            jc.each {
//                Contract d = it
//                d.sfly = true
//                d.save(failOnError: true)
//            }
//
//            StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("F"))
//            dto.indexNum = CreateNumberService.getRandomNumber(new StringBuffer(former))
//            def rc=registerContractResourceService.create(dto)
//            rc.indexNum = CreateNumberService.getFullNumber(former, dto.id.toString())
//            rc.save(failOnError: true)
//            result.put("rest_status", restStatus)
//            result.put("rest_result", rc as JSON)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        }else {
//            result.put("rest_information", rest_information)
//            result.put("rest_status", restStatus)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        }
    }

    //归还
    @POST
    @Path('/returnContract')
    Response returnContract(RegisterContract dto){
        ok {
            def fund = dto.fund
            dto.actionType = true
            JSONObject result = new JSONObject();
            String restStatus = REST_STATUS_SUC;
            int sn = Integer.parseInt(dto.startNum.substring(5))
            int en = Integer.parseInt(dto.endNum.substring(5))
            JSONArray jc = new JSONArray()
            for (int i = sn;i <= en;i++){
                Contract c = Contract.findBySzbhAndSflyAndFund(i, true, fund)
                if (null == c){
                    restStatus = "err"
                    print(restStatus)
                    break
                }else {
                    print("put "+i)
                    jc.put(c)
                }
            }
            if ("suc" == restStatus){
                jc.each {
                    Contract d = it
                    d.sfly = false
                    d.save(failOnError: true)
                }

                StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("F"))
                dto.indexNum = CreateNumberService.getRandomNumber(new StringBuffer(former))
                def rc=registerContractResourceService.create(dto)
                rc.indexNum = CreateNumberService.getFullNumber(former, dto.id.toString())
                rc.save(failOnError: true)
                result.put("rest_status", restStatus)
                result.put("rest_result", rc as JSON)
                return result
            }else {
                result.put("rest_status", restStatus)
                return result
            }
        }
//        String rest_information = REST_INFORMATION
//        //{"indexNum":"0001","department":{"id":1}, "receiveUser":{"id":1}, "receiveDate":"2014-12-16T08:00:00Z", "fund":{"id":1}, "startNum":"CFYHN201", "endNum":"CFYHN250", "total":50}
////        {"department":{"id":1},"receiveUser":{"id":1},"receiveDate":"2014-12-16T08:00:00Z","fund":{"id":1},"startNum":"CFYHN201","endNum":"CFYHN250","total":50}
//        print("returnContract RegisterContractResourceService")
//        def fund = dto.fund
//        dto.actionType = true
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        int sn = Integer.parseInt(dto.startNum.substring(5))
//        int en = Integer.parseInt(dto.endNum.substring(5))
//        JSONArray jc = new JSONArray()
//        for (int i = sn;i <= en;i++){
//            Contract c = Contract.findBySzbhAndSflyAndFund(i, true, fund)
//            if (null == c){
//                rest_information = "非法的合同编号!"
//                restStatus = "err"
//                print(restStatus)
//                break
//            }else {
//                print("put "+i)
//                jc.put(c)
//            }
//        }
//        if ("suc" == restStatus){
//            jc.each {
//                Contract d = it
//                d.sfly = false
//                d.save(failOnError: true)
//            }
//
//            StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("F"))
//            dto.indexNum = CreateNumberService.getRandomNumber(new StringBuffer(former))
//            def rc=registerContractResourceService.create(dto)
//            rc.indexNum = CreateNumberService.getFullNumber(former, dto.id.toString())
//            rc.save(failOnError: true)
//            result.put("rest_status", restStatus)
//            result.put("rest_result", rc as JSON)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        }else {
//            result.put("rest_information", rest_information)
//            result.put("rest_status", restStatus)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        }
    }

}
