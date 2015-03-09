package com.jsy.archives

import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Finfo
import com.jsy.utility.CreateNumberService
import com.jsy.utility.GetYieldService
import grails.converters.JSON
import org.json.JSONArray
import org.json.JSONObject

import javax.ws.rs.DefaultValue
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/investmentArchives')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class InvestmentArchivesCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def investmentArchivesResourceService
    def getYieldService

    //根据档案id取附件
    @GET
    @Path('/getFiles')
    Response getgetFiles(@QueryParam('archiveNum') String archiveNum){
        InvestmentArchives investmentArchives=InvestmentArchives.findByArchiveNum(archiveNum)
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        result.put("rest_status", restStatus)
        result.put("rest_result", investmentArchives.uploadFiles as JSON )
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    //获取剩余的收益额
    @GET
    @Path('/getProceeds')
    Response getProceeds(@QueryParam('archiveNum') String archiveNum){
        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray()
        String restStatus = REST_STATUS_SUC;
        boolean b = true
        try {
            InvestmentArchives investmentArchives = InvestmentArchives.findByArchiveNum(archiveNum)

            if ("N" == investmentArchives.fxfs) {
                JSONObject obj = new JSONObject()
                BigDecimal lx = investmentArchives.sjtzje * investmentArchives.nhsyl
                obj.put("lx", lx)
                obj.put("fxsj", investmentArchives.fxsj1)
                if (investmentArchives.fxsj1 && investmentArchives.fxsj1.after(new Date())) {
                    b = false
                }
                obj.put("sffx", b)
                jsonArray.put(obj)
            } else if (investmentArchives.fxfs == "W") {
                BigDecimal lx = investmentArchives.sjtzje * investmentArchives.nhsyl / 2
                JSONObject obj1 = new JSONObject()
                obj1.put("lx", lx)
                obj1.put("fxsj", investmentArchives.fxsj1)
                obj1.put("sffx", true)
                JSONObject obj2 = new JSONObject()
                obj2.put("lx", lx)
                obj2.put("fxsj", investmentArchives.fxsj2)
                obj2.put("sffx", true)
                if (investmentArchives.fxsj1 && investmentArchives.fxsj1.after(new Date())) {
                    obj1.put("sffx", false)
                }
                if (investmentArchives.fxsj2 && investmentArchives.fxsj2.after(new Date())) {
                    obj2.put("sffx", false)
                }
                jsonArray.put(obj1)
                jsonArray.put(obj2)
            } else if (investmentArchives.fxfs == "J") {
                BigDecimal lx = investmentArchives.sjtzje * investmentArchives.nhsyl / 4
                JSONObject obj1 = new JSONObject()
                obj1.put("lx", lx)
                obj1.put("fxsj", investmentArchives.fxsj1)
                obj1.put("sffx", true)
                JSONObject obj2 = new JSONObject()
                obj2.put("lx", lx)
                obj2.put("fxsj", investmentArchives.fxsj2)
                obj2.put("sffx", true)
                JSONObject obj3 = new JSONObject()
                obj3.put("lx", lx)
                obj3.put("fxsj", investmentArchives.fxsj3)
                obj3.put("sffx", true)
                JSONObject obj4 = new JSONObject()
                obj4.put("lx", lx)
                obj4.put("fxsj", investmentArchives.fxsj4)
                obj4.put("sffx", true)
                if (investmentArchives.fxsj1 && investmentArchives.fxsj1.after(new Date())) {
                    obj1.put("sffx", false)
                }
                if (investmentArchives.fxsj2 && investmentArchives.fxsj2.after(new Date())) {
                    obj2.put("sffx", false)
                }
                if (investmentArchives.fxsj3 && investmentArchives.fxsj3.after(new Date())) {
                    obj3.put("sffx", false)
                }
                if (investmentArchives.fxsj4 && investmentArchives.fxsj4.after(new Date())) {
                    obj4.put("sffx", false)
                }
                jsonArray.put(obj1)
                jsonArray.put(obj2)
                jsonArray.put(obj3)
                jsonArray.put(obj4)
            }
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            e.printStackTrace()
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", jsonArray.toString())
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @GET
    @Path('/getFile')
    Response getFile(@QueryParam('archiveNum') String archiveNum){
        InvestmentArchives investmentArchives=InvestmentArchives.findByArchiveNum(archiveNum)
        JSONObject result = new JSONObject();
        JSONArray fxsj=new JSONArray()

        //客户名字
        result.put("username",investmentArchives.username)
        //基金名
        result.put("fundName",investmentArchives.fundName)
        //投资金额
        result.put("sjtzje",investmentArchives.sjtzje)
        //投资期限
        result.put("qx",investmentArchives.qx)
        //投资预期收益率
        result.put("yqsyl",investmentArchives.yqsyl)
        //计息开始日期
        result.put("startDate",investmentArchives.startDate)
        //计息结束日期
        result.put("endDate",investmentArchives.endDate)
        //第一次付息时间
        if(investmentArchives.fxsj1){
            fxsj.put(investmentArchives.fxsj1)
        }
        //第二次付息时间
        if(investmentArchives.fxsj2){
            fxsj.put(investmentArchives.fxsj2)
        }
        //第三次付息时间
        if(investmentArchives.fxsj3){
            fxsj.put(investmentArchives.fxsj3)
        }
        //第四次付息时间
        if(investmentArchives.fxsj4){
            fxsj.put(investmentArchives.fxsj4)
        }
        result.put("fxsj",fxsj)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build();
    }

    @PUT
    @Path('/CreateOrUpdate')
    Response CreateOrUpdate(InvestmentArchives dto,@QueryParam('id') @DefaultValue("") String id) {
//        测试数据
//        {"customer":{"name":"12","credentialsNumber":"12","country":"china","phone":"12",country":"12","credentialsType":"12","telephone":"12","postalcode":"12","email":"12","callAddress":"12","remark":"12"},"contractNum":"12","fund":"1","tzje":"12","tzqx":"1","rgrq":"2014-12-23T14:49:20Z","dqrq":"2014-12-23T14:49:20Z","fxfs":"12","htzt":"1","ywjl":"1"}
//        {"customer":{"name":"12","khh":"12","yhzh":"12","country":"12","credentialsType":"12","credentialsNumber":"12","telephone":"12","phone":"12","postalcode":"12","email":"12","callAddress":"12","remark":"12"},"contractNum":"122301","fund":"1","tzje":"12","tzqx":"1","rgrq":"2014-12-23T18:17:43Z","dqrq":"2014-12-23T18:17:43Z","fxfs":"12","htzt":"1","ywjl":"1"}
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ia

        if ("" == id || null == id){
            try {
                Customer cus=null
                if(!(dto.customer.credentialsNumber==null||dto.customer.credentialsNumber=="")){
                    cus=dto.customer.save(failOnError: true)
                    CustomerArchives cusa=CustomerArchives.findByCredentialsNumber(dto.customer.credentialsNumber)
                    if(!cusa) {
                        CustomerArchives customerArchives = new CustomerArchives()
                        customerArchives.properties = cus.properties
                        customerArchives.save(failOnError: true)
                    }
//                    }else{
//                        cus.properties=dto.customer.properties
//                        cus.save(failOnError: true)
//                    }
                    dto.status=1
                    dto.username=cus.name
                }
                dto.bj=dto.tzje
                dto.customer=cus
                StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("I"))
                dto.archiveNum = CreateNumberService.getRandomNumber(new StringBuffer(former))
                dto.markNum = dto.archiveNum
                dto = investmentArchivesResourceService.create(dto)
                dto.archiveNum = CreateNumberService.getFullNumber(former, dto.id.toString())

                dto.markNum = dto.archiveNum
                dto.save(failOnError: true)
            }catch (Exception e){
                restStatus = REST_STATUS_FAI
                print(e)
            }
            result.put("rest_status", restStatus)
            result.put("rest_result", dto as JSON)

            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

        }else{
//            try {
            Customer cus=null
            if(!(dto.customer.credentialsNumber==null||dto.customer.credentialsNumber=="")){
//                cus=Customer.findByCredentialsNumber(dto.customer.credentialsNumber)
//                if(!cus){
                cus=dto.customer.save(failOnError: true)
//                }else{
//                    cus.properties=dto.customer.properties
//                    cus.save(failOnError: true)
                }
                dto.status=1
                dto.username=cus.name
//            }
            dto.customer=cus
            ia = investmentArchivesResourceService.update(dto,Integer.parseInt(id))
//            }catch (Exception e){
//                restStatus = REST_STATUS_FAI
//                print(e)
//            }
            result.put("rest_status", restStatus)
            result.put("rest_result", ia as JSON)

            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

        }



    }

    @GET
    @Path('/GetById')
    Response GetById(@QueryParam('id') Long id){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ia
        try {
            ia = InvestmentArchives.get(id)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ia as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    //根据名字模糊查询投资确认书列表
    @GET
    @Path('/getAllFile')
    Response getAllFile(@QueryParam('query') String query){
        List<InvestmentArchives> investmentArchives=InvestmentArchives.findAllByFundNameLikeOrUsernameLike("%"+query+"%","%"+query+"%")
        JSONArray result=new JSONArray();
        investmentArchives.each {
            JSONObject ia = new JSONObject();
            //客户名字
            ia.put("username",it.username)
            //基金名
            ia.put("fundName",it.fundName)
            //投资金额
            ia.put("sjtzje",it.sjtzje)
            //认购日期
            ia.put("rgrq",it.rgrq)
            //投资期限
            ia.put("qx",it.qx)
            //投资预期收益率
            ia.put("yqsyl",it.yqsyl)
            //付息方式
            ia.put("fxfs",it.fxfs)
            //部门
            ia.put("bm",it.bmjl.username)
            //最近打印日期
            ia.put("zjdysj",it.zjdysj)
            //文档编号
            ia.put("archiveNum",it.archiveNum)
            result.put(ia);
        }
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build();
    }

    @PUT
    @Path("/updateforprint")
    Response updateforprint(@QueryParam('id') Long id){
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ia
        try {
            ia = InvestmentArchives.get(id)
            ia.dycs = ia.dycs + 1
            ia.zjdysj = new Date()
            ia.save(failOnError: true)

            //添加业务提成
            def commissionInfo=CommissionInfo.findAllByArchivesId(ia.id)
            if(commissionInfo.size()==0){
                ia.ywtcs.each {
                    //业务提成金额
                    new CommissionInfo(zfsj:new Date(),lx: 0, fundName:ia.fund.fundName,customer: ia.username,tzje: ia.tzje,syl: ia.nhsyl,archivesId:ia.id,rgqx: ia.tzqx,rgrq: ia.rgrq,tcr: it.user.chainName,ywjl: ia.ywjl.chainName,skr: it.skr,yhzh: it.yhzh,khh: it.khh,sfgs: !it.user.isUser,tcje: it.tcje,tcl:ia.ywtc ).save(failOnError: true)
                }
            }

        }catch (Exception e){
        restStatus = REST_STATUS_FAI
        print(e)
    }
    result.put("rest_status", restStatus)
    result.put("rest_result", ia as JSON)
    return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    //打印更新接口还有奖励领了，然后
    @POST
    @Path('/print1')
    Response print1(@QueryParam('archiveNum') String archiveNum){
        InvestmentArchives investmentArchives=InvestmentArchives.findByArchiveNum(archiveNum)
        //更新打印数据
        if (investmentArchives){
            investmentArchives.dycs=investmentArchives.dycs+1
            investmentArchives.zjdysj=new Date()
            investmentArchives.save(failOnError: true)
            return Response.ok("{'result':'ok'}").status(200).build();
        }else{
            return Response.ok("{'result':'error'}").status(200).build();
        }

    }

    @GET
    @Path('/getYield')
    Response getYield(@QueryParam('fundid') Long fundid,
                      @QueryParam('managerid') Long managerid,
                      @QueryParam('investment') BigDecimal investment,
                      @QueryParam('vers') String vers){

        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def gy
        try {
            gy = GetYieldService.getYield(fundid, managerid, investment,vers)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", gy )
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Finfo finfo) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        int total
        def ia
        ia = investmentArchivesResourceService.findByParm(finfo.keyword)
        total = ia.size()
        try {
            JSONObject json = investmentArchivesResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            total = json.get("size")
            ia = json.get("page")
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ia)
        result.put("rest_total", total)



        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    def formatClassToValue(JSONObject object){
       def objectClass=object.keys()
        return  object;
    }

    @GET
    Response readAll() {
        ok investmentArchivesResourceService.readAll()
    }

    @Path('/{id}')
    InvestmentArchivesResource getResource(@PathParam('id') Long id) {
        new InvestmentArchivesResource(investmentArchivesResourceService: investmentArchivesResourceService, id: id)
    }

    @POST
    @Path('/IAOutput')
    Response IAOutput(Finfo finfo){
        if (null == finfo.keyword){
            finfo.keyword = ""
        }
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        int total
        def ia
        org.codehaus.groovy.grails.web.json.JSONArray iao = new org.codehaus.groovy.grails.web.json.JSONArray()
        try {

            ia = investmentArchivesResourceService.IAOutput(finfo.pagesize, finfo.startposition, finfo.keyword).get("page")
            total = investmentArchivesResourceService.IAOutput(finfo.pagesize, finfo.startposition, finfo.keyword).get("size")
            ia.each{
//            InvestmentArchives inves = it
                if (null != it.customer) {
                    IAOutput iaoo = new IAOutput(it)
                    iao.put(iaoo)
                }else {
                    print(it.toString() + ".customer is null!!!")
                }
            }

        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }


        print("customer NOT NULL.size = " + iao.size())
        result.put("rest_status", restStatus)
        result.put("rest_result", iao as JSON)
        result.put("rest_total", total)

        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

}
