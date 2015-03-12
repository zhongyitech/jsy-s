package com.jsy.project

import com.jsy.bankConfig.BankAccount
import com.jsy.fundObject.Fund
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

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
            // get project
            org.json.JSONObject obj = JSON.parse(datastr)

            Fund fund = Fund.get(obj.fundid)
            TSProject project = TSProject.get(obj.projectid)
            BankAccount bankAccount = BankAccount.get(obj.bankid)
            def paydate = Date.parse("yyyy-MM-dd", obj.paydate)
            def payTargets = obj.targets
            def payRecords = obj.payRecords
            def paytotal = obj.paytotal
            def remain_money_suggest = new BigDecimal(obj.remain_money_suggest)


            //check same suggest： remain_money_suggest
            def paytotal2 = new BigDecimal(obj.paytotal)
            def receiveDetails = [:]
            obj.payRecords?.each{payRecordId->
                PayRecord payRecord = PayRecord.get(payRecordId)
                if(payRecord){
                    obj.targets?.each{target->
                        if("main_money".equals(target)){
                            receiveDetails.put(target,payRecord.amount)
                            paytotal2= paytotal2 - payRecord.amount
                            println paytotal2+","+payRecord.amount+","+(paytotal2 - payRecord.amount)
                        }else if("interest_money".equals(target)){
                            receiveDetails.put(target,payRecord.interest_bill)
                            paytotal2= paytotal2 - payRecord.interest_bill
                        }else if("manage_money".equals(target)){
                            receiveDetails.put(target,payRecord.manage_bill)
                            paytotal2= paytotal2 - payRecord.manage_bill
                            println paytotal2
                        }else if("community_money".equals(target)){
                            receiveDetails.put(target,payRecord.community_bill)
                            paytotal2= paytotal2 - payRecord.community_bill
                        }else if("borrow_money".equals(target)){
                            receiveDetails.put(target,payRecord.borrow_bill)
                            paytotal2= paytotal2 - payRecord.borrow_bill
                        }else if("penalty_money".equals(target)){
                            receiveDetails.put(target,payRecord.penalty_bill)
                            paytotal2= paytotal2 - payRecord.penalty_bill
                        }else if("over_money".equals(target)){
                            def dueMoney = payRecord.getOverDue()
                            receiveDetails.put(target,dueMoney)
                            paytotal2= paytotal2 - dueMoney
                        }
                    }
                }
            }

            if(paytotal2!=remain_money_suggest){
                throw new Exception("error remain count in front!")
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

    @Path('/{id}')
    ReceiveRecordResource getResource(@PathParam('id') Long id) {
        new ReceiveRecordResource(receiveRecordResourceService: receiveRecordResourceService, id:id)
    }
}
