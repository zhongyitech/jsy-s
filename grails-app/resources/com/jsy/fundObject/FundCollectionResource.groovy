package com.jsy.fundObject

import com.jsy.auth.AuthorityService
import com.jsy.project.PayRecord
import com.jsy.project.ReceiveDetailRecord
import com.jsy.project.ReceiveRecord
import com.jsy.project.ShouldReceiveRecord
import com.jsy.utility.CreateNumberService
import com.jsy.utility.DomainHelper
import com.jsy.utility.MyResponse
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.management.Query
import javax.ws.rs.*
import javax.ws.rs.core.Response
import java.text.SimpleDateFormat

import static org.grails.jaxrs.response.Responses.created

import static com.jsy.utility.MyResponse.*


@Path('/api/fund')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class FundCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    public static final String NULL_STATUS = "200"
    def fundResourceService
    AuthorityService authorityService

    //新增基金
    @PUT
    Response create(Fund dto) {
        ok {
            Date d = new Date()
            dto.createDate = d
            dto.kcwyjbl = 0.05
            StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("F"))
            dto.fundNo = CreateNumberService.getRandomNumber(new StringBuffer(former))
            def fund = fundResourceService.create(dto)
            fund.fundNo = CreateNumberService.getFullNumber(former, dto.id.toString())
            fund.save(failOnError: true)
            return fund
        }
//        print("FundCollectionResource.create")
//        JSONObject result = new JSONObject();
//        JSONArray table = new JSONArray();
//        String restStatus = REST_STATUS_SUC;
//        Fund fund
//        try {
//            Date d = new Date()
//            dto.createDate = d
//            dto.kcwyjbl = 0.05
//            StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("F"))
//            dto.fundNo = CreateNumberService.getRandomNumber(new StringBuffer(former))
//            fund = fundResourceService.create(dto)
//            fund.fundNo = CreateNumberService.getFullNumber(former, dto.id.toString())
//            fund.save(failOnError: true)
//
//            result.put("rest_status", restStatus)
//            result.put("rest_result", fund as JSON)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        } catch (Exception e) {
//            restStatus = REST_STATUS_FAI;
//            e.printStackTrace()
//            result.put("rest_status", restStatus)
//            result.put("rest_result", fund as JSON)
//            return Response.ok(result.toString()).status(500).build()
//        }
////        result.put("rest_status", restStatus)
////        result.put("rest_result", fund as JSON)
////        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//
////        return fundResourceService.read(dto.fundName)
    }

    //test接口
    @PUT
    @Path('/test')
    Response test() {
        Fund fund = new Fund()
        fund.fundName = "测试基金16"
        fund.fundNo = "16"
        created fundResourceService.create(fund)
    }

    //删除基金
    @DELETE
    Response delete(@QueryParam('id') Long id) {
        ok{
            return  fundResourceService.delete(id);
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def dto
//        try {
//            dto = fundResourceService.delete(id)
//        } catch (Exception e) {
//            restStatus = REST_STATUS_FAI;
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", dto)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    //更新基金
    @PUT
    @Path('/update')
    Response update(Fund dto, @QueryParam('id') Long id) {

        ok{
            return fundResourceService.update(dto, id)
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def fund
//        try {
//            fund = fundResourceService.update(dto, id)
//            result.put("rest_status", restStatus)
//            result.put("rest_result", fund as JSON)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        } catch (Exception e) {
//            restStatus = REST_STATUS_FAI;
//            e.printStackTrace()
//            result.put("rest_status", restStatus)
//            result.put("rest_result", fund as JSON)
//            return Response.ok(result.toString()).status(500).build()
//        }
////        result.put("rest_status", restStatus)
////        result.put("rest_result", fund as JSON)
////        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    //读取全部基金
    @GET
    Response readAll() {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def fund
        try {
            fund = authorityService.getAuth(fundResourceService.readAll())
        } catch (Exception e) {
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", fund)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    /**
     * 返回基金列表,专用接口(用于前台Select扩展功能)
     * @return $.dom.select('id=",[data]) 专用数据
     */
    @GET
    @Path('/selectList')
    Response selectList(@QueryParam("exInclude") Long fundid){
        com.jsy.utility.MyResponse.ok{
            def data=[]
            //todo:最好能优化为只从数据库中返回指定字段的方法
            Fund.findAllByIdNotEqual(fundid).each{
                data.push([id:it.id,mapName:it.fundName,])
            }
            data
        }
    }


    //读取主页数据
    @POST
    @Path('/mainPage')
    Response readMainPage(Finfo f1) {
        print('/mainPage/readMainPage')
        print(f1.startposition)
        Finfo f = new Finfo()
        print(f)
        if (null != f1.status) {
            f.status = f1.status
        }
        if (null != f1.startsaledate1) {
            f.startsaledate1 = f1.startsaledate1
        }
        if (null != f1.startsaledate2) {
            f.startsaledate2 = f1.startsaledate2
        }
        if (null != f1.keyword) {
            f.keyword = f1.keyword
        }
        if (null != f1.startposition) {
            f.startposition = f1.startposition
        }
        if (null != f1.pagesize) {
            f.pagesize = f1.pagesize
        }
//    Response readMainPage(@QueryParam('pagesize') @DefaultValue('10') Long pagesize, @QueryParam('startposition') @DefaultValue('0') Long startposition, @QueryParam('keyword') String keyword, @QueryParam('status') @DefaultValue("200") int status, @QueryParam('startsaledate1') @DefaultValue('2000-01-01 00:00:00') String startsaledate1, @QueryParam('startsaledate2') @DefaultValue('3000-01-01 00:00:00')  String startsaledate2)  {
//        f.pagesize = 2
//        f.startposition = 0
//        f.keyword = ""
//        f.status = 0
//        f.startsaledate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-12-11 00:00:00")
//        f.startsaledate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-12-31 00:00:00")

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        Date date2 = new Date()
        //example 2014-12-09 09:10:46
//        if (null == f.startsaledate2 || "" == f.startsaledate2){
//
//            sdf.format(date2)
//
//        }else {
//            date2 = sdf.parse(f.startsaledate2)
//
//        }

//        String da=sdf.format(startsaledate1)
//        Date date1 = sdf.parse(f.startsaledate1)
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = REST_STATUS_SUC;
        int total
        JSONObject json
        def rc
        try {
            //分页内容查找
//        def funcoll = fundResourceService.readMainPage(pagesize, startposition, keyword, status, date1, date2)
            json = fundResourceService.readMainPage(f.pagesize, f.startposition, f.keyword, f.status, f.startsaledate1, f.startsaledate2)
            total = json.get("size")
            rc = json.get("page")
//        print(funcoll.size)
//
            def fund_all = fundResourceService.readAll()
            int fund_count = fund_all.size() //统计总数
            int raise_count = 0    //在募基金数量
            long plan_total = 0     //预计募集总量
            long real_total = 0     //实际募集总量
            long season_plan_total = 0      //季度预募总量
            long season_real_total = 0      //季度实募总量
            long half_plan_total = 0        //半年预募总量
            long half_real_total = 0        //半年实募总量
            long year_plan_total = 0        //年度预募总量
            long year_real_total = 0        //年度实募总量
            //遍历
            fund_all.each {
                Fund current_fund = it
                if (1 == current_fund.status) {
                    raise_count++
                }

                if (current_fund.raiseFunds.toString().isNumber()) {
                    plan_total += current_fund.raiseFunds
                }
                if (null == current_fund.rRaiseFunds) {
//                real_total += current_fund.rRaiseFunds
                } else {
                    real_total += current_fund.rRaiseFunds
                }
                if (current_fund.quarterRaise.toString().isNumber()) {
                    season_plan_total += current_fund.quarterRaise
                }
                if (current_fund.rQuarterRaise.toString().isNumber()) {
                    season_real_total += current_fund.rQuarterRaise
                }
                if (current_fund.halfRaise.toString().isNumber()) {
                    half_plan_total += current_fund.halfRaise
                }
                if (current_fund.rHalfRaise.toString().isNumber()) {
                    half_real_total += current_fund.rHalfRaise
                }
                if (current_fund.yearRaise.toString().isNumber()) {
                    year_plan_total += current_fund.yearRaise
                }
                if (current_fund.rYearRaise.toString().isNumber()) {
                    year_real_total += current_fund.rYearRaise
                }

            }
            result.put("fund_count", fund_count)
            result.put("raise_count", raise_count);
            result.put("plan_total", plan_total)
            result.put("real_total", real_total)
            result.put("season_plan_total", season_plan_total)
            result.put("season_real_total", season_real_total)
            result.put("half_plan_total", half_plan_total)
            result.put("half_real_total", half_real_total)
            result.put("year_plan_total", year_plan_total)
            result.put("year_real_total", year_real_total)
        } catch (Exception e) {
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", rc as JSON)
        result.put("rest_total", total)

//        print("funcoll class: "+funcoll.getClass().getName())
//        String resultString = result.toString();
//        StringEntity resultEntity = new StringEntity(resultString);
//        return b.build(EntityUtils.toString(resultEntity,"UTF-8"));
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build();
    }

    /*
    * @GET
    @Path('/user({username})/getById')
    Response readAllById(@PathParam('username') String username,
                         @QueryParam('max') Long max,
                         @QueryParam('id') Long id) {
        ok trainingResourceService.readAllById(username,max,id)
        //URLexample: /user({username})/getById?max=1&id=2
    }
    * */

    @GET
    @Path('/{id}')
    FundResource getResource(@PathParam('id') Long id) {
        new FundResource(fundResourceService: fundResourceService, id: id)
    }

//    @POST
//    @Path('/getTable')
//    Response findByParm(Fund dto) {
//        ok fundResourceService.findByParm(Fund dto)
//    }

    @GET
    @Path("/getKxzqx")
    Response getKxzqx(@QueryParam('id') Long id) {
        JSONObject result = new JSONObject();
        result.put("rest_status", 200)
        result.put("rest_result", Fund.get(id).kxzqx as JSON)
        return Response.ok(result.toString()).status(200).build()
    }

    @GET
    @Path('/nameLike')
    Response findByNameLike(@QueryParam('params') String username,@QueryParam('extraData') String jsonData) {
        def users = Fund.findAllByFundNameLike("%" + username + "%")
        org.json.JSONArray jsonArray = new org.json.JSONArray()
        users.each {
            JSONObject jso = new JSONObject()
            jso.put("value", it.fundName)
            jso.put("data", it.id)
            jsonArray.put(jso)
        }
        JSONObject jsonObject = new JSONObject()
        jsonObject.put("query", "Unit")
        jsonObject.put("suggestions", jsonArray)

        return Response.ok(jsonObject.toString()).status(RESPONSE_STATUS_SUC).build();
    }

    @GET
    @Path('/getHurry')
    Response getHurry(@QueryParam('fundid') String fundid) {
        MyResponse.ok {
            Fund fund = Fund.get(fundid)
            if(fund){
                def rtn = [:]
                if(fund.funcCompany?.companyName){
                    rtn.companyName=fund.funcCompany?.companyName
                }else{
                    rtn.companyName="未确定"
                }

                rtn.fundName=fund.fundName
                rtn.id=fund.id
                def should_payback = new BigDecimal(0)
                def main_payback = new BigDecimal(0)
                def interest_payback = new BigDecimal(0)
                def overdue_payback = new BigDecimal(0)
                PayRecord.findAllByFundAndArchive(fund,false).each{
                    should_payback+=it.totalBalance()
                    main_payback+=(it.amount-it.payMainBack)
                    interest_payback+=(it.interest_bill-it.interest_pay)
                    overdue_payback+=(it.getOverDue()-it.overDue_pay)
                }

                //汇入银行
                rtn.bankAccount="无法确定"
                rtn.should_payback=should_payback
                rtn.main_payback=main_payback
                rtn.interest_payback=interest_payback
                rtn.overdue_payback=overdue_payback
                rtn.nowDate=new Date()
                return rtn
            }else{
                throw new Exception("no fund found.")
            }

        }
    }

    @GET
    @Path('/getInteract')
    Response getInteract(@QueryParam('fundid') String fundid) {
        MyResponse.ok {
            Fund fund = Fund.get(fundid)
            if(fund){
                def rtn = [:]
                if(fund.funcCompany?.companyName){
                    rtn.companyName=fund.funcCompany?.companyName
                }else{
                    rtn.companyName="未确定"
                }
                rtn.fundName=fund.fundName
                rtn.id=fund.id
                rtn.nowDate=new Date()

                def main_pay = new BigDecimal(0)
                def total_receive = new BigDecimal(0)
                def total_manage = new BigDecimal(0)
                def total_channel = new BigDecimal(0)
                def total_main = new BigDecimal(0)

                def details = []
                PayRecord.findAllByFundAndArchive(fund,false).each{
                    main_pay+= it.amount

                    def detail = [:]
                    detail.payDate = it.payDate
                    detail.amount = it.amount
                    ReceiveDetailRecord maintain = ReceiveDetailRecord.findByPayRecordAndTargetAndArchive(it,"maintain",false);
                    ReceiveDetailRecord channel = ReceiveDetailRecord.findByPayRecordAndTargetAndArchive(it,"channel",false);
                    if(maintain){
                        detail.maintain_date=maintain.dateCreated
                        detail.maintain=it.manage_pay
                    }
                    if(channel){
                        detail.channel_date=channel.dateCreated
                        detail.channel=it.community_pay
                    }
                    details << detail
                }


                ReceiveRecord.findAllByFundAndArchive(fund,false).each{
                    total_receive+=it.amount
                    ReceiveDetailRecord.findAllByReceiveRecordAndArchive(it,false).each{receive->
                        if("maintain".equals(receive.target)){
                            total_manage+=receive.amount
                        }else if("channel".equals(receive.target)){
                            total_channel+=receive.amount
                        }else if("original".equals(receive.target)){
                            total_main+=receive.amount
                        }
                    }
                }
                rtn.main_pay=main_pay;
                rtn.total_receive=total_receive;
                rtn.total_manage=total_manage;
                rtn.total_channel=total_channel;
                rtn.total_main=total_main;
                rtn.details=details;

                return rtn
            }else{
                throw new Exception("no fund found.")
            }

        }
    }


}

