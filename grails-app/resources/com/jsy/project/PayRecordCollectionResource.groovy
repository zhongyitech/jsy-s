package com.jsy.project

import com.jsy.bankConfig.BankAccount
import com.jsy.fundObject.Fund
import com.jsy.util.Utils
import com.jsy.utility.MyResponse
import grails.converters.JSON
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

@Path('/api/payRecord')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class PayRecordCollectionResource {

    PayRecordResourceService payRecordResourceService


    @POST
    @Path('/readAllForPage')
    Response readAllForPage(String criteriaStr) {
        org.json.JSONObject result = new org.json.JSONObject();
        String restStatus = 200;
        int total
        def results
        try {
            if(criteriaStr && !"".equals(criteriaStr)){
                def rtn = payRecordResourceService.readAllForPage(criteriaStr)
                if(rtn){
                    total = rtn.total
                    results= rtn.results
                }
            }else{
                restStatus = "500";
            }
        }catch (Exception e){
            restStatus = "500";
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", results as JSON)
        result.put("rest_total", total)

        return Response.ok(result.toString()).status(200).build()
    }

    @POST
    @Path('/create')
    Response create(PayRecord dto) {
//        created payRecordResourceService.create(dto)


        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";

        try{

            payRecordResourceService.create(dto)

            result.put("rest_status", restStatus)
            result.put("rest_result", dto as JSON)
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            print(e)
            result.put("rest_status", restStatus)
            result.put("rest_result", dto as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
    }

    @POST
    @Path('/add_pay_record')
    Response addPayRecord(String datastr) {
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";

        // get project
        org.json.JSONObject obj = JSON.parse(datastr)

        try{
            //数据校验
            if(obj.fundid==""||!obj.fundid){
                result.put("rest_status", "error")
                result.put("rest_result", "no fund set!")
                return Response.ok(result.toString()).status(500).build()
            }

            if(obj.project==""||obj.project=="null"||!obj.project){
                result.put("rest_status", "error")
                result.put("rest_result", "no project set!")
                return Response.ok(result.toString()).status(500).build()
            }

            //数据保存
            Fund fund = Fund.get(obj.fundid)
            TSProject project = TSProject.get(obj.project)
            BankAccount bankAccount = BankAccount.get(obj.bankselect)
            def paydate = Date.parse("yyyy-MM-dd", obj.paydate)


            PayRecord dto = new PayRecord(payDate:paydate,amount:obj.paytotal,payType:obj.moneyUseType,project:project,fund:fund,bankAccount:bankAccount);
            payRecordResourceService.create(dto)

            result.put("rest_status", restStatus)
            result.put("rest_result", "")
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", e.getLocalizedMessage())
            return Response.ok(result.toString()).status(500).build()
        }
    }



    @GET
    @Path('/loadPayRecordsByProject')
    Response loadPayRecordsByProject(@QueryParam('projectid') int projectid) {
        def project = TSProject.get(projectid)
        if(!project){
            return Response.ok("no project found").status(500).build()
        }
        def payRecords = PayRecord.findAllByProjectAndArchive(project, false)
        def rtn = []
        payRecords.each{payRecord->
            def pay_record = [:]

            pay_record.payType = payRecord.payType
            pay_record.amount = payRecord.amount
            pay_record.payDate = payRecord.payDate

            pay_record.id = payRecord.id
            pay_record.manage_pay = payRecord.manage_bill                               //管理费
            pay_record.community_pay = payRecord.community_bill                         //渠道费
            pay_record.borrow_pay = payRecord.borrow_bill                               //借款

            pay_record.interest_year1 = project.year1
            pay_record.interest_pay = payRecord.interest_bill                           //第一年利率
            pay_record.dateCount=Utils.dayDifferent(payRecord.payDate,new Date())       //投资天数


//            singleCount", "costCount", "dayCount

            //这里开始计算逾期利息
            Date nowDate = new Date()
            Date lastDate = Utils.addYears(payRecord.payDate,Integer.parseInt(new java.text.DecimalFormat("0").format((project.year1 + project.year2))))

            if(nowDate.after(lastDate)){//判断超出预定时间，查询时发现超出，就修改penalty_pay
                pay_record.penalty_pay = payRecord.amount * project.penalty_per         //违约金
                payRecord.penalty_bill=pay_record.penalty_pay
                payRecord.save(failOnError: true)
            }else{
                pay_record.penalty_pay = 0 //返回结果用
            }

            pay_record.over_interest_pay = payRecord.getOverDue()
            rtn << pay_record
        }

        ok rtn
    }



    @Path('/{id}')
    PayRecordResource getResource(@PathParam('id') Long id) {
        new PayRecordResource(payRecordResourceService: payRecordResourceService, id:id)
    }

    @POST
    @Path('/changeByDate')
    Response changeByDate(String datastr) {
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = "200";

        // get project
        org.json.JSONObject obj = JSON.parse(datastr)



        try{
            //数据校验
//            if(obj.has("payRecords")){
//                result.put("rest_status", "error")
//                result.put("rest_result", "no payRecords set!")
//                return Response.ok(result.toString()).status(500).build()
//            }

//            if(obj.has("stopDate")){
//                result.put("rest_status", "error")
//                result.put("rest_result", "no stopDate set!")
//                return Response.ok(result.toString()).status(500).build()
//            }

            //
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            def stopDate = sf.parse(obj.stopDate)

            def results = []
            obj.payRecords.each{
                results.push(PayRecord.get(it))
            }
            results = results.collect {payRecord->
                payRecord.getShowProperties(stopDate);
            }

            result.put("rest_status", restStatus)
            result.put("rest_result", results as JSON)
            return Response.ok(result.toString()).status(200).build()
        }catch (Exception e){
            restStatus = "500";
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", e.getLocalizedMessage())
            return Response.ok(result.toString()).status(500).build()
        }
    }

    /**
     * 删除不是一个可逆的过程，一旦删除，里面的累计数值不能作为参考
     * @param payRecordId
     * @return
     */
    @POST
    @Path('/del')
    Response del(@QueryParam('payRecordId') Long payRecordId) {
        MyResponse.ok {
            PayRecord payRecord = PayRecord.get(payRecordId)
            if(payRecord){
                payRecordResourceService.delPayRecord(payRecord)
                return true;
            }else{
                return false;
            }
        }
    }

}
