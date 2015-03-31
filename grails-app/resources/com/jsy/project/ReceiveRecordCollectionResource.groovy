package com.jsy.project

import com.jsy.bankConfig.BankAccount
import com.jsy.fundObject.Fund
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.QueryParam

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

    def receiveRecordResourceService

    @POST
    @Path('/add_receive_record')
    Response create(String datastr) {
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";

        try{
            //json格式数据转换
            org.json.JSONObject obj = JSON.parse(datastr)

            //基础数据转换
            Fund fund = Fund.get(obj.fundid)
            TSProject project = TSProject.get(obj.projectid)
            BankAccount bankAccount = BankAccount.get(obj.bankid)
            def paydate = Date.parse("yyyy-MM-dd", obj.paydate)


            def paytotal = obj.paytotal
            def remain_money_suggest = new BigDecimal(obj.remain_money_suggest)


            //根据前台的计算结果，进行再次验证，check same suggest： remain_money_suggest
            def _paytotal = new BigDecimal(obj.paytotal)
            def receiveDetails = []


            if(obj.invest_struct && obj.invest_struct.targets && obj.invest_struct.payRecords ){
                _paytotal = consumePayRecords(_paytotal, obj.invest_struct, receiveDetails);
            }

            if(obj.borrow_struct && obj.borrow_struct.targets && obj.borrow_struct.payRecords){
                _paytotal = consumePayRecords(_paytotal, obj.borrow_struct, receiveDetails);
            }


            if(_paytotal!=remain_money_suggest){
                throw new Exception("error remain count in front!$_paytotal vs $remain_money_suggest")
            }

            //数据保存
            ReceiveRecord dto = new ReceiveRecord(receiveDate:paydate,amount:paytotal,
                    project:project,fund:fund,bankAccount:bankAccount,remain_charge:remain_money_suggest);

            receiveRecordResourceService.create(dto,receiveDetails)

            result.put("rest_status", restStatus)
            result.put("rest_result", dto as JSON)
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            e.printStackTrace()
            print(e)
            result.put("rest_status", restStatus)
            result.put("rest_result", "error")
            return Response.ok(result.toString()).status(500).build()
        }
    }

    def consumePayRecords(_paytotal, invest_struct, receiveDetails){

        def payTargets = invest_struct.targets
        def payRecords = invest_struct.payRecords

        //保证顺序，dirty operate!!!
        invest_struct.targets?.sort { targetA,targetB->
            return targetA.id.compareTo(targetB.id)
        }

        invest_struct.payRecords?.each{payRecordId->
            PayRecord payRecord = PayRecord.get(payRecordId)
            if(payRecord){

                invest_struct.targets?.each{target->
                    //target的顺序很重要呢
                    if("main_money".equals(target.name)){
                        ReceiveDetailRecord detailRecord

                        if(_paytotal>payRecord.amount){//有多余的钱
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: payRecord.amount,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                        }else if(_paytotal>0){
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: _paytotal,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                            payRecord.payMainBack = _paytotal
                        }
                        _paytotal= _paytotal - payRecord.amount


                    }else if("interest_money".equals(target.name)){
                        ReceiveDetailRecord detailRecord

                        if(_paytotal>payRecord.interest_bill){//有多余的钱
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: payRecord.interest_bill,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                        }else if(_paytotal>0){
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: _paytotal,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                        }
                        _paytotal= _paytotal - payRecord.interest_bill

                    }else if("manage_money".equals(target.name)){
                        ReceiveDetailRecord detailRecord

                        if(_paytotal>payRecord.manage_bill){//有多余的钱
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: payRecord.manage_bill,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                        }else if(_paytotal>0){
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: _paytotal,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                        }
                        _paytotal= _paytotal - payRecord.manage_bill
                    }else if("community_money".equals(target.name)){
                        ReceiveDetailRecord detailRecord

                        if(_paytotal>payRecord.community_bill){//有多余的钱
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: payRecord.community_bill,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                        }else if(_paytotal>0){
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: _paytotal,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                        }
                        _paytotal= _paytotal - payRecord.community_bill
                    }else if("borrow_money".equals(target.name)){
                        ReceiveDetailRecord detailRecord

                        if(_paytotal>payRecord.borrow_bill){//有多余的钱
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: payRecord.borrow_bill,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                        }else if(_paytotal>0){
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: _paytotal,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                        }
                        _paytotal= _paytotal - payRecord.borrow_bill
                    }else if("penalty_money".equals(target.name)){
                        ReceiveDetailRecord detailRecord

                        if(_paytotal>payRecord.penalty_bill){//有多余的钱
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: payRecord.penalty_bill,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                        }else if(_paytotal>0){
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: _paytotal,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                        }
                        _paytotal= _paytotal - payRecord.penalty_bill
                    }else if("over_money".equals(target.name)){
                        ReceiveDetailRecord detailRecord
                        def dueMoney = payRecord.getOverDue()

                        if(_paytotal>dueMoney){//有多余的钱
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: dueMoney,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                        }else if(_paytotal>0){
                            detailRecord = new ReceiveDetailRecord(target: target.name, amount: _paytotal,payRecord:payRecord);
                            receiveDetails.push(detailRecord)
                        }
                        _paytotal= _paytotal - dueMoney
                    }
                }
            }
        }

        return _paytotal;
    }


    @GET
    Response readAll() {
        ok receiveRecordResourceService.readAll()
    }

    @GET
    @Path('/findByPayRecord')
    Response findByPayRecord(@QueryParam('payRecordId') Long payRecordId) {
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";

        try{

            PayRecord payRecord = PayRecord.get(payRecordId)
            if(!payRecord){
                result.put("rest_status", restStatus)
                result.put("rest_result", "no pay record found")
                return Response.ok(result.toString()).status(500).build()
            }


            def receiveDetails = ReceiveDetailRecord.findAllByPayRecord(payRecord)

            result.put("rest_status", restStatus)
            result.put("rest_result", receiveDetails as JSON)
            result.put("rest_totalBalance", payRecord.totalBalance())
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            result.put("rest_status", restStatus)
            result.put("rest_result", "error")
            return Response.ok(result.toString()).status(500).build()
        }

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
            if(!payRecord){
                result.put("rest_status", restStatus)
                result.put("rest_result", "no pay record found")
                return Response.ok(result.toString()).status(500).build()
            }


            def receiveDetails = ReceiveDetailRecord.findAllByPayRecord(payRecord)

            result.put("rest_status", restStatus)
            result.put("rest_result", receiveDetails as JSON)
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
}
