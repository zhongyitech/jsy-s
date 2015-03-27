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

            def payTargets = obj.targets
            def payRecords = obj.payRecords
            def paytotal = obj.paytotal
            def remain_money_suggest = new BigDecimal(obj.remain_money_suggest)

            //保证顺序，dirty operate!!!
            obj.targets.sort { targetA,targetB->
                return targetA.id.compareTo(targetB.id)
            }

            //根据前台的计算结果，进行再次验证，check same suggest： remain_money_suggest
            def paytotal2 = new BigDecimal(obj.paytotal)
            def receiveDetails = []
            obj.payRecords?.each{payRecordId->
                PayRecord payRecord = PayRecord.get(payRecordId)
                if(payRecord){

                    obj.targets?.each{target->
                        //target的顺序很重要呢
                        if("main_money".equals(target.name)){
                            ReceiveDetailRecord detailRecord

                            if(paytotal2>payRecord.amount){//有多余的钱
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: payRecord.amount,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                            }else if(paytotal2>0){
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: paytotal2,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                                payRecord.payMainBack = paytotal2
                            }
                            paytotal2= paytotal2 - payRecord.amount

                        }else if("interest_money".equals(target.name)){
                            ReceiveDetailRecord detailRecord

                            if(paytotal2>payRecord.interest_bill){//有多余的钱
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: payRecord.interest_bill,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                            }else if(paytotal2>0){
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: paytotal2,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                            }
                            paytotal2= paytotal2 - payRecord.interest_bill

                        }else if("manage_money".equals(target.name)){
                            ReceiveDetailRecord detailRecord

                            if(paytotal2>payRecord.manage_bill){//有多余的钱
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: payRecord.manage_bill,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                            }else if(paytotal2>0){
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: paytotal2,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                            }
                            paytotal2= paytotal2 - payRecord.manage_bill
                        }else if("community_money".equals(target.name)){
                            ReceiveDetailRecord detailRecord

                            if(paytotal2>payRecord.community_bill){//有多余的钱
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: payRecord.community_bill,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                            }else if(paytotal2>0){
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: paytotal2,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                            }
                            paytotal2= paytotal2 - payRecord.community_bill
                        }else if("borrow_money".equals(target.name)){
                            ReceiveDetailRecord detailRecord

                            if(paytotal2>payRecord.borrow_bill){//有多余的钱
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: payRecord.borrow_bill,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                            }else if(paytotal2>0){
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: paytotal2,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                            }
                            paytotal2= paytotal2 - payRecord.borrow_bill
                        }else if("penalty_money".equals(target.name)){
                            ReceiveDetailRecord detailRecord

                            if(paytotal2>payRecord.penalty_bill){//有多余的钱
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: payRecord.penalty_bill,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                            }else if(paytotal2>0){
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: paytotal2,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                            }
                            paytotal2= paytotal2 - payRecord.penalty_bill
                        }else if("over_money".equals(target.name)){
                            ReceiveDetailRecord detailRecord
                            def dueMoney = payRecord.getOverDue()

                            if(paytotal2>dueMoney){//有多余的钱
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: dueMoney,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                            }else if(paytotal2>0){
                                detailRecord = new ReceiveDetailRecord(target: target.name, amount: paytotal2,payRecord:payRecord);
                                receiveDetails.push(detailRecord)
                            }
                            paytotal2= paytotal2 - dueMoney
                        }
                    }
                }
            }

            if(paytotal2!=remain_money_suggest){
                throw new Exception("error remain count in front!$paytotal2 vs $remain_money_suggest")
            }

            //数据保存
            ReceiveRecord dto = new ReceiveRecord(receiveDate:paydate,amount:paytotal,payTargets:payTargets,
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
}
