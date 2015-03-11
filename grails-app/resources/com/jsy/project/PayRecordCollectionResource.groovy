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

@Path('/api/payRecord')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class PayRecordCollectionResource {

    def payRecordResourceService

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
        def payRecords = PayRecord.findAllByProject(project)
        def rtn = []
        payRecords.each{payRecord->
            def pay_record = [:]
            pay_record.amount = payRecord.amount
            pay_record.payDate = payRecord.payDate

            pay_record.id = payRecord.id
            pay_record.manage_pay = payRecord.amount * project.manage_per                       //管理费
            pay_record.community_pay = payRecord.amount * project.community_per                 //渠道费
            pay_record.borrow_pay = payRecord.amount * project.borrow_per                       //借款
            pay_record.penalty_pay = payRecord.amount * project.penalty_per                     //违约金
            pay_record.interest_year1 = project.year1
            pay_record.interest_pay = payRecord.amount * project.interest_per * project.year1   //第一年利率
            pay_record.dateCount=dayDifferent(payRecord.payDate,new Date())                     //投资天数
//            singleCount", "costCount", "dayCount

            //这里开始计算逾期利息
            Date nowDate = new Date()
            Date lastDate = addYears(payRecord.payDate,Integer.parseInt(new java.text.DecimalFormat("0").format((project.year1 + project.year2))))

            if(nowDate.after(lastDate)){//判断超出预定时间
                def owe_money = payRecord.amount - payRecord.payMainBack
                def over_days = dayDifferent(lastDate,nowDate)
                if(owe_money > 0){
                    if("singleCount".equals(project.interestType)){//单利：欠款*interest_per/365*超出的天数
                        pay_record.over_interest_pay = (owe_money * project.interest_per * over_days / 365)
                    }else if("costCount".equals(project.interestType)){//单利：(欠款+欠款*interest_per)*penalty_per/365*超出的天数
                        pay_record.over_interest_pay = (owe_money * (1+project.interest_per) * over_days / 365)
                    }else if("dayCount".equals(project.interestType)){//复利：便历每一天，做加法：第一天:(欠款+欠款*penalty_per)*penalty_per/365*1 ,第二天：第一天的利息*penalty_per/365*1，如此类推
                        pay_record.over_interest_pay=(owe_money * (1+project.interest_per) / 365);  //第一天
                        for(int i=1;i<over_days;i++){//第二天起
                            pay_record.over_interest_pay += (pay_record.over_interest_pay * (1+project.interest_per) / 365);
                        }
                    }
                }
            }else{
                pay_record.over_interest_pay = 0
            }
            rtn << pay_record
        }

        ok rtn
    }

    Date addYears(final java.sql.Timestamp date, final int years) {
        Date calculatedDate = null;

        if (date != null) {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, years);
            calculatedDate = calendar.getTime()
        }

        return calculatedDate;
    }

    int dayDifferent(Date dateStart,Date dateStop) {
        if(dateStart.after(dateStop)){
            throw new Exception("dateStart after dateStop");
        }

        //毫秒ms
        long diff = dateStop.getTime() - dateStart.getTime();

        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffDays
    }


    @Path('/{id}')
    PayRecordResource getResource(@PathParam('id') Long id) {
        new PayRecordResource(payRecordResourceService: payRecordResourceService, id:id)
    }
}
