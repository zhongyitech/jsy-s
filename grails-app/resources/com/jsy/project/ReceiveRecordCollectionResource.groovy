package com.jsy.project

import com.jsy.bankConfig.BankAccount
import com.jsy.fundObject.Fund
import com.jsy.util.OrderProperty
import com.jsy.util.SearchProperty
import com.jsy.utility.MyResponse
import grails.converters.JSON
import grails.gorm.DetachedCriteria
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.QueryParam
import java.text.SimpleDateFormat

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/receiveRecord')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class ReceiveRecordCollectionResource {

    ReceiveRecordResourceService receiveRecordResourceService

    @POST
    @Path('/add_receive_record')
    Response create(String datastr) {

        MyResponse.ok{
            //json格式数据转换
            org.json.JSONObject obj = JSON.parse(datastr)

            def dto = receiveRecordResourceService.create(obj)

            return dto
        }

    }


    @GET
    Response readAll() {
        ok receiveRecordResourceService.readAll()
    }

    @GET
    @Path('/findByPayRecord')
    Response findByPayRecord(@QueryParam('payRecordId') Long payRecordId,@QueryParam('stopDate') String stopDate) {
        MyResponse.ok {
            PayRecord payRecord = PayRecord.get(payRecordId)
            if(!payRecord && !payRecord.archive){
                throw new Exception("找不到该付款记录，payRecordId："+payRecordId)
            }

            SimpleDateFormat sf =new SimpleDateFormat("yyyy-MM-dd")
            Date _stopDate = sf.parse(stopDate)

            def receiveDetails = ReceiveDetailRecord.findAllByPayRecordAndArchive(payRecord, false)

            def rtn = [:]
            rtn.rest_result = receiveDetails
            rtn.rest_totalBalance = payRecord.totalBalance(_stopDate)
            return rtn
        }

    }

    @POST
    @Path('/findReceiveByFund')
    Response findReceiveByFund(String criteriaStr) {
        org.json.JSONObject obj = JSON.parse(criteriaStr)

        def criterib = new DetachedCriteria(ReceiveRecord).build {
            //and
            eq("fund",Fund.get(obj.get("fundid")))
            eq("archive",false)

            //orderby
//            Object orderByObj = obj.get("orderby-prperties")
//            JSONArray array3 = (JSONArray)orderByObj;
//            if(array3.size()>0){
//                or {
//                    array3.each{property->
//                        OrderProperty p =new OrderProperty(property);
//                        order(p.key,p.value)
//                    }
//                }
//            }
            order("dateCreated","desc")
        }

        def params = [:]
        params.max = Math.min(obj.get("page").max?.toInteger() ?: 25, 100)
        params.offset = obj.get("page").offset ? obj.get("page").offset.toInteger() : 0

        def results = criterib.list(params)
        results = results.collect {receiveRecord->
            receiveRecord.getShowProperties();
        }
        def total = criterib.size()

        org.json.JSONObject result = new org.json.JSONObject();
        String restStatus = 200;
        result.put("rest_status", restStatus)
        result.put("rest_result", results as JSON)
        result.put("rest_total", total)

        return Response.ok(result.toString()).status(200).build()

    }

    @Path('/{id}')
    ReceiveRecordResource getResource(@PathParam('id') Long id) {
        new ReceiveRecordResource(receiveRecordResourceService: receiveRecordResourceService, id:id)
    }


    @GET
    @Path('/shouldReceiveDetail')
    Response shouldReceiveDetail(@QueryParam('payRecordId') Long payRecordId) {
        JSONObject result = new JSONObject();
        String restStatus = "200";

        try{

            PayRecord payRecord = PayRecord.get(payRecordId)
            if(!payRecord && !payRecord.archive){
                result.put("rest_status", restStatus)
                result.put("rest_result", "no pay record found")
                return Response.ok(result.toString()).status(500).build()
            }

            def shouldReceiveRecords = ShouldReceiveRecord.findAllByPayRecordAndAmountGreaterThanAndArchive(payRecord,0,false)

            result.put("rest_status", restStatus)
            result.put("rest_result", shouldReceiveRecords as JSON)
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            result.put("rest_status", restStatus)
            result.put("rest_result", "error")
            return Response.ok(result.toString()).status(500).build()
        }

    }


    @GET
    @Path('/autoGenOverDateRecord')
    Response autoGenOverDateRecord() {
        JSONObject result = new JSONObject();
        String restStatus = "200";

        try{
            receiveRecordResourceService.autoGenOverDateRecord()

            result.put("rest_status", restStatus)
            result.put("rest_result", "done" as JSON)
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            result.put("rest_status", restStatus)
            result.put("rest_result", "error")
            return Response.ok(result.toString()).status(500).build()
        }


    }

    @POST
    @Path('/del')
    Response del(@QueryParam('recvRecordId') Long recvRecordId) {
        MyResponse.ok {
            ReceiveRecord recvRecord = ReceiveRecord.get(recvRecordId)
            if(recvRecord){
                receiveRecordResourceService.delRecvRecord(recvRecord)
                return true;
            }else{
                return false;
            }
        }
    }
}
