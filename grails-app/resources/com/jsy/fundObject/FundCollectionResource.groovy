package com.jsy.fundObject

import com.jsy.utility.CreateNumberService
import grails.converters.JSON
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.*
import javax.ws.rs.core.Response
import java.text.SimpleDateFormat

import static org.grails.jaxrs.response.Responses.created
import static org.grails.jaxrs.response.Responses.ok

@Path('/api/fund')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class FundCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    public static  final  String NULL_STATUS = "200"
    def fundResourceService

    //新增基金
    @POST
    @Path('/createfund')
    Response create(Fund dto) {
        print("FundCollectionResource.create")
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        String restStatus = REST_STATUS_SUC;
        Fund fund
        try{
            Date d = new Date()
            dto.createDate = d
            dto.kcwyjbl=0.05
            StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("F"))
            dto.fundNo = CreateNumberService.getRandomNumber(new StringBuffer(former))
            fund=fundResourceService.create(dto)
            fund.fundNo = CreateNumberService.getFullNumber(former, dto.id.toString())
            fund.save(failOnError: true)

            result.put("rest_status", restStatus)
            result.put("rest_result", fund as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            e.printStackTrace()
            print(e)
            result.put("rest_status", restStatus)
            result.put("rest_result", fund as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", fund as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

//        return fundResourceService.read(dto.fundName)
    }



    //test接口
    @PUT
    @Path('/test')
    Response test(){
        Fund fund = new Fund()
        fund.fundName = "测试基金16"
        fund.fundNo = "16"
        created fundResourceService.create(fund)
    }


    //删除基金
    @DELETE
    Response delete(@QueryParam('id') Long id) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def dto
        try{
            dto = fundResourceService.delete(id)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", dto)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }


    //更新基金
    @PUT
    @Path('/update')
    Response update(Fund dto,@QueryParam('id') Long id) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def fund
        try{
            fund = fundResourceService.update(dto,id)
            result.put("rest_status", restStatus)
            result.put("rest_result", fund as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", fund as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", fund as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    //读取全部基金
    @GET
    Response readAll() {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def fund
        String s="1"
        try {
            print("read all fund")
            print(fundResourceService.seachByCriteria("fundNo='F201501311'",[max: 10, offset: 5]))
            fund = fundResourceService.readAll()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", fund as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    //读取主页数据
    @POST
    @Path('/mainPage')
    Response readMainPage(Finfo f1)  {
        print('/mainPage/readMainPage')
        print(f1.startposition)
        Finfo f = new Finfo()
        print(f)
        if (null != f1.status){
            f.status = f1.status
        }
        if (null != f1.startsaledate1){
            f.startsaledate1 = f1.startsaledate1
        }
        if (null != f1.startsaledate2){
            f.startsaledate2 = f1.startsaledate2
        }
        if (null != f1.keyword){
            f.keyword = f1.keyword
        }
        if (null != f1.startposition){
            f.startposition = f1.startposition
        }
        if (null != f1.pagesize){
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
        try{
        //分页内容查找
//        def funcoll = fundResourceService.readMainPage(pagesize, startposition, keyword, status, date1, date2)
            json = fundResourceService.readMainPage(f.pagesize, f.startposition, f.keyword, f.status, f.startsaledate1, f.startsaledate2)
            total = json.get("size")
            print(total)
            rc = json.get("page")
            print(rc)

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
            if(1 == current_fund.status){
                raise_count++
            }

            if(current_fund.raiseFunds.toString().isNumber()){
                plan_total += current_fund.raiseFunds
            }
            if(null == current_fund.rRaiseFunds){
//                real_total += current_fund.rRaiseFunds
            }else{
                real_total += current_fund.rRaiseFunds
            }
            if(current_fund.quarterRaise.toString().isNumber()){
                season_plan_total += current_fund.quarterRaise
            }
            if(current_fund.rQuarterRaise.toString().isNumber()) {
                season_real_total += current_fund.rQuarterRaise
            }
            if(current_fund.halfRaise.toString().isNumber()) {
                half_plan_total += current_fund.halfRaise
            }
            if(current_fund.rHalfRaise.toString().isNumber()) {
                half_real_total += current_fund.rHalfRaise
            }
            if(current_fund.yearRaise.toString().isNumber()) {
                year_plan_total += current_fund.yearRaise
            }
            if(current_fund.rYearRaise.toString().isNumber()) {
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
        }catch (Exception e){
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
        new FundResource(fundResourceService: fundResourceService, id:id)
    }

//    @POST
//    @Path('/getTable')
//    Response findByParm(Fund dto) {
//        ok fundResourceService.findByParm(Fund dto)
//    }

    @GET
    @Path("/getKxzqx")
    Response getKxzqx(@QueryParam('id') Long id)  {
        JSONObject result = new JSONObject();
        result.put("rest_status", 200)
        result.put("rest_result", Fund.get(id).kxzqx as JSON)
        return Response.ok(result.toString()).status(200).build()
    }

    @GET
    @Path('/nameLike')
    Response findByNameLike(@QueryParam('params') String username){
        def users=Fund.findAllByFundNameLike("%"+username+"%")
        org.json.JSONArray jsonArray=new org.json.JSONArray()
        users.each {
            JSONObject jso=new JSONObject()
            jso.put("value",it.fundName)
            jso.put("data",it.id)
            jsonArray.put(jso)
        }
        JSONObject jsonObject=new JSONObject()
        jsonObject.put("query","Unit")
        jsonObject.put("suggestions",jsonArray)

        ok jsonObject.toString()
    }
}

