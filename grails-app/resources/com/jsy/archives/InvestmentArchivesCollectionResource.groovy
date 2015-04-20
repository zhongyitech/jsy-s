package com.jsy.archives

import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Finfo
import com.jsy.fundObject.Fund
import com.jsy.system.TypeConfig
import com.jsy.utility.CreateNumberService
import com.jsy.utility.DomainHelper
import com.jsy.utility.GetYieldService
import com.jsy.utility.JsonResult
import com.jsy.utility.MyResponse
import grails.converters.JSON
import org.json.JSONArray
import org.json.JSONObject

import javax.ws.rs.DefaultValue
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam
import java.text.SimpleDateFormat

import static org.grails.jaxrs.response.Responses.*
import com.jsy.utility.*

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
    InvestmentArchivesResourceService investmentArchivesResourceService
    def getYieldService
    def paymentInfoResourceService
    CustomerArchivesResourceService customerArchivesResourceService
    //根据档案id取附件
    @GET
    @Path('/getFiles')
    Response getgetFiles(@QueryParam('archiveNum') String archiveNum) {
        InvestmentArchives investmentArchives = InvestmentArchives.findByArchiveNum(archiveNum)
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        result.put("rest_status", restStatus)
        result.put("rest_result", investmentArchives.uploadFiles as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    //获取剩余的收益额
    @GET
    @Path('/getProceeds')
    Response getProceeds(@QueryParam('archiveNum') String archiveNum) {
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
        } catch (Exception e) {
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
    static Response getFile(@QueryParam('archiveNum') String archiveNum) {
        InvestmentArchives investmentArchives = InvestmentArchives.findByArchiveNum(archiveNum)
        JSONObject result = new JSONObject();
        JSONArray fxsj = new JSONArray()

        //客户名字
        result.put("username", investmentArchives.username)
        //基金名
        result.put("fundName", investmentArchives.fundName)
        //投资金额
        result.put("sjtzje", investmentArchives.sjtzje)
        //投资期限
        result.put("qx", investmentArchives.qx)
        //投资预期收益率
        result.put("yqsyl", investmentArchives.yqsyl)
        //计息开始日期
        result.put("startDate", investmentArchives.startDate)
        //计息结束日期
        result.put("endDate", investmentArchives.endDate)
        //第一次付息时间
        if (investmentArchives.fxsj1) {
            fxsj.put(investmentArchives.fxsj1)
        }
        //第二次付息时间
        if (investmentArchives.fxsj2) {
            fxsj.put(investmentArchives.fxsj2)
        }
        //第三次付息时间
        if (investmentArchives.fxsj3) {
            fxsj.put(investmentArchives.fxsj3)
        }
        //第四次付息时间
        if (investmentArchives.fxsj4) {
            fxsj.put(investmentArchives.fxsj4)
        }
        result.put("fxsj", fxsj)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build();
    }

    //检测传入MODEL的合法性，属性的值
    def ValidationModel(InvestmentArchives dto) {

        def error = [:]
        if (dto.fxfs == null || dto.fxfs.length() > 1) {
            error.fxfs = "付息方式参数不合法"
        }
        if (dto.dqrq == null || dto.dqrq <= dto.rgrq) error.dqrq = "到期日期不合法"

        if (!investmentArchivesResourceService.checkContractNumberIVisible(dto.contractNum)) {
            error.contractNum = "合同编号没有登记"
        }

        def contract = Contract.findByHtbh(dto.contractNum)
        if (contract != null && contract.fund.id != dto.fund.id) {
            error.fund = "选择的基金与合同编号的注册基金不一致"
        }

        if (dto.fund != null) {
            def fund = Fund.get(dto.fund.id)
            if (fund != null && fund.yieldRange.size() == 0) {
                //检测收益率是否与传入的参数一致
                error.nhsyl = "年化收益参数不合法"
            }
        } else {
            error.fund = "基金对象不能为空"
        }
        return error
    }

    /**
     * 创建或是更新投资档案信息
     * @param dto
     * @param id
     * @return
     */
    @PUT
    @Path('/CreateOrUpdate')
    Response CreateOrUpdate(InvestmentArchives dto, @QueryParam('id') @DefaultValue("") String id) {
        MyResponse.ok {

            def ia
            dto.bmjl = dto.ywjl.department.leader
            dto.bm = dto.bmjl.department.deptName

            //create
            if ("" == id || null == id) {
                def exception = investmentArchivesResourceService.IVisible(dto.contractNum)
                if (exception != null) {
                    throw exception
                }
                Customer cus = null
                if (!(dto.customer == null || dto.customer.credentialsNumber == null || dto.customer.credentialsNumber == "")) {
                    cus = dto.customer.save(failOnError: true)

                    CustomerArchives cusa = CustomerArchives.findByCredentialsNumber(dto.customer.credentialsNumber)

                    if (!cusa) {
                        CustomerArchives customerArchives = new CustomerArchives()
                        customerArchives.properties = cus.properties
                        if (!customerArchives.zch) {
                            customerArchives.zch = ""
                        }

                        customerArchives.save(failOnError: true)
                        TypeConfig typeConfig = TypeConfig.findByTypeAndMapValue(7, 2)
                        customerArchives.addToBankAccount(bankName: cus.khh, bankOfDeposit: cus.khh, accountName: cus.name, account: cus.yhzh, purpose: typeConfig).save(failOnError: true)
                    }
                    dto.status = 1
                    dto.username = cus.name
                }
                dto.bj = dto.tzje
                dto.customer = cus
                StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("I"))
                dto.archiveNum = CreateNumberService.getRandomNumber(new StringBuffer(former))
                dto.markNum = dto.archiveNum
                dto = investmentArchivesResourceService.create(dto)
                dto.archiveNum = CreateNumberService.getFullNumber(former, dto.id.toString())
                dto.markNum = dto.archiveNum
                dto.save(failOnError: true)
                //付息时间新增
                List times = investmentArchivesResourceService.scfxsj(dto.rgrq, dto.tzqx, dto.fxfs)
                int i = 1
                times.each {
                    PayTime payTime = new PayTime(px: i, fxsj: it, sffx: false, investmentArchives: dto).save(failOnError: true)
                    dto.addToPayTimes(payTime).save(failOnError: true)
                    i++
                }
                return dto

            } else {
                //update
                Customer cus = null
                if (!(dto.customer == null || dto.customer.credentialsNumber == null || dto.customer.credentialsNumber == "")) {
                    cus = dto.customer.save(failOnError: true)
                    dto.status = 1
                    dto.username = cus.name
                }
                dto.customer = cus
                ia = investmentArchivesResourceService.update(dto, Integer.parseInt(id))
                return ia
            }
        }
    }

    /**
     * 为档案完善客户信息
     * @param dto
     * @param id
     * @return
     */
    @POST
    @Path('/customer')
    Response customerEdit(Customer dto, @QueryParam('id') Long id, @QueryParam('sync') Boolean sync) {
        MyResponse.ok {
            def ia = InvestmentArchives.get(id)
            def old = ia.customer
            def obj = null
            if (old) {
                //update
                old.properties = dto.properties
                old.save(failOnError: true)
                ia.username = ia.customer.name
                obj = old
            } else {
                //save
                ia.customer = dto.save(failOnError: true)
                ia.username = ia.customer.name
                obj = ia.customer
            }
            ia.save(failOnError: true)
            if (sync) {
                customerArchivesResourceService.copyCustomer(obj);
            }
            return obj
        }
    }

    @GET
    @Path('/GetPayTimes')
    Response getPayTimes(@QueryParam('startTime') String startTime,
                         @QueryParam('qx') String qx,
                         @QueryParam('fxfs') String fxfs) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
            ok JsonResult.success(investmentArchivesResourceService.scfxsj(date, qx, fxfs))
        } catch (Exception e) {
            print(e)
            ok JsonResult.error(e.message)
        }
    }

    @GET
    @Path('/GetById')
    Response GetById(@QueryParam('id') Long id) {
        MyResponse.ok {
            return investmentArchivesResourceService.read(id)
        }
    }

    @GET
    @Path('/getByIdSpecial')
    Response getByIdSpecial(@QueryParam('id') Long id) {
        MyResponse.ok {
            def result = [:]
            def iv = InvestmentArchives.get(id)
            if (iv) {
                result.putAt("id", iv.id)
//                print(InvestmentArchives.class.name)
//                result.putAt("class",InvestmentArchives.class.name)
                result.putAll(iv.properties)
                return iv
            }
            return null
        }
    }


    //根据名字模糊查询投资确认书列表
    @GET
    @Path('/getAllFile')
    Response getAllFile(@QueryParam('query') String query) {
        List<InvestmentArchives> investmentArchives = InvestmentArchives.findAllByFundNameLikeOrUsernameLike("%" + query + "%", "%" + query + "%")
        JSONArray result = new JSONArray();
        investmentArchives.each {
            JSONObject ia = new JSONObject();
            //客户名字
            ia.put("username", it.username)
            //基金名
            ia.put("fundName", it.fundName)
            //投资金额
            ia.put("sjtzje", it.sjtzje)
            //认购日期
            ia.put("rgrq", it.rgrq)
            //投资期限
            ia.put("qx", it.qx)
            //投资预期收益率
            ia.put("yqsyl", it.yqsyl)
            //付息方式
            ia.put("fxfs", it.fxfs)
            //部门
            ia.put("bm", it.bmjl.username)
            //最近打印日期
            ia.put("zjdysj", it.zjdysj)
            //文档编号
            ia.put("archiveNum", it.archiveNum)
            result.put(ia);
        }
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build();
    }

    @PUT
    @Path("/updateforprint")
    Response updateforprint(@QueryParam('id') Long id) {
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
        def ia
        try {
            ia = InvestmentArchives.get(id)
            ia.dycs = ia.dycs + 1
            ia.zjdysj = new Date()
            ia.save(failOnError: true)

            //添加业务提成
            def commissionInfo = CommissionInfo.findAllByArchivesId(ia.id)
            if (commissionInfo.size() == 0) {
                ia.ywtcs.each {
                    //业务提成金额
                    new CommissionInfo(zfsj: new Date(), lx: 0, fundName: ia.fund.fundName, customer: ia.username, tzje: ia.tzje, syl: ia.nhsyl, archivesId: ia.id, rgqx: ia.tzqx, rgrq: ia.rgrq, tcr: it.user.chainName, ywjl: ia.ywjl.chainName, skr: it.skr, yhzh: it.yhzh, khh: it.khh, sfgs: !it.user.isUser, tcje: it.tcje, tcl: ia.ywtc).save(failOnError: true)
                }
            }
            ok JsonResult.success(ia)

        } catch (Exception e) {
//            restStatus = REST_STATUS_FAI
            print(e)
            ok JsonResult.error(e.message)
        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", ia as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    //打印更新接口还有奖励领了，然后
    @POST
    @Path('/print1')
    Response print1(@QueryParam('archiveNum') String archiveNum) {
        InvestmentArchives investmentArchives = InvestmentArchives.findByArchiveNum(archiveNum)
        //更新打印数据
        if (investmentArchives) {
            investmentArchives.dycs = investmentArchives.dycs + 1
            investmentArchives.zjdysj = new Date()
            investmentArchives.save(failOnError: true)
            return Response.ok("{'result':'ok'}").status(200).build();
        } else {
            return Response.ok("{'result':'error'}").status(200).build();
        }
    }

    /**
     * 更新投资档案关联的客户的信息
     * @param dto 客户数据
     * @param id 要更新的客户ID
     * @return 更新后的数据
     */
    @POST
    @Path('')
    Response editCustomer(Customer dto, @QueryParam('cid') Long cid, @QueryParam('iid') Long iid) {
        MyResponse.ok {
            def archives = InvestmentArchives.get(iid)
            if (archives == null || archives.customer.id != cid) {
                throw new Exception("提交数据不合法,客户")
            }

        }
    }

    @GET
    @Path('/getyy')
    Response Getyy() {
        ok JsonResult.success("ok")
    }

    @GET
    @Path('/getYield')
    Response getYield(@QueryParam('fundid') Long fundid,
                      @QueryParam('managerid') Long managerid,
                      @QueryParam('investment') BigDecimal investment,
                      @QueryParam('vers') String vers) {
        def gy
        try {
            gy = GetYieldService.getYield(fundid, managerid, investment, vers)
            ok JsonResult.success(gy)
        } catch (Exception e) {
            print(e)
            ok JsonResult.error(e.message)
        }
    }

    /**
     * 返回指定业务经理的提成和年化信息
     * @param fundid 基金ID
     * @param userid 业务经理ID
     * @param amount 投资金额
     * @param vers 合同版本
     * @return
     */
    @GET
    @Path('/getYieldInfo')
    Response getYieldForUser(@QueryParam('fundId') Long fundId,
                             @QueryParam('userId') Long userId,
                             @QueryParam('amount') BigDecimal amount,
                             @QueryParam('ver') String ver) {
        MyResponse.ok {
            def user = User.get(userId)
            def leader = user.department.leader
            return GetYieldService.getYield(fundId, leader.id, amount, ver)
        }
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
        } catch (Exception e) {
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", ia)
        result.put("rest_total", total)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    def formatClassToValue(JSONObject object) {
        def objectClass = object.keys()
        return objectClass;
    }

    @GET
    Response readAll() {
//        ok investmentArchivesResourceService.readAll()
        ok JsonResult.success(investmentArchivesResourceService.readAll())
    }

    @Path('/{id}')
    InvestmentArchivesResource getResource(@PathParam('id') Long id) {
        new InvestmentArchivesResource(investmentArchivesResourceService: investmentArchivesResourceService, id: id)
    }

    void addKey(Map target, def it, def valueName) {
        target.putAt(it.key, (it.value == null ? null : it.value[valueName]))
    }

    @POST
    @Path('/IAOutput')
    Response IAOutput(Map finfo) {
        MyResponse.page {
            def dc = DomainHelper.getDetachedCriteria(InvestmentArchives, finfo)
            def data = []
            dc.list([max: finfo.pagesize, offset: finfo.startposition]).each {
                def row = [:]
                row.putAt("id", it.id)

                it.properties.each {
                    switch (it.key) {
                        case "customer":
//                            addKey(row, it, "name")
                            row.putAt(it.key, it.value == null ? null : [name: it.value.name, country: it.value.country])
                            break
                        case "fund":
                            row.putAt(it.key, it.value.fundName)
                            break
                        case "ywjl":
                            row.putAt(it.key, it.value.chainName)
                            break
                        default:
                            row.putAt(it.key, it.value)
                    }

                }
                def pay = paymentInfoResourceService.getPaymentAmount(it.id)
                row.putAt("bj", pay["bj"])
                row.putAt("lx", pay["lx"])
                data.push(row)
            }
            return [data: data, total: dc.count()]
        }
    }

    /**
     * 获取投资档案信息(以合同编号 为依据的信息)
     * @param arg
     * @return
     */
    @POST
    @Path('/ArchivesByNO')
    Response GetHeTongStatusList(Map arg) {
        MyResponse.page {
            def dc = DomainHelper.getDetachedCriteria(InvestmentArchives, arg)
            List<InvestmentArchives> investmentArchives = dc
                    .list([max: arg.pagesize, offset: arg.startposition])
            def res = []
            investmentArchives.each {
                def row = [:]
                row.contractNum = it.contractNum
                row.htzt = it.htzt.mapName
                row.customer = it.username
                row.rgrq = it.rgrq
                row.tzqx = it.tzqx
                row.tzje = it.sjtzje
                row.fundName = it.fund.fundName

                //下次提成日期和金额
                def today = new Date()
                Date next_tc_time = null
                def next_tc_amount = 0
                it.ywtcs.sort {
                    a, b -> a.tcffsj < b.tcffsj
                }.each {
                    if (it.tcffsj > today) {
                        next_tc_time = it.tcffsj
                    }
                }
                //获取管理提成信息
                def gl_list = investmentArchivesResourceService.getGltcList(it)
                def next_gltc = gl_list.find {
                    it.time > today
                }
                if (next_gltc != null && next_gltc.time > today) {
                    next_tc_time = next_gltc.time
                    next_tc_amount = next_gltc.amount
                }
                //设置下次提成数据
                row.next_tc_time = next_tc_time
                row.next_tc_amount = next_tc_amount

                Date n_day
                it.payTimes.each {
                    print(it.fxsj)
                    if (it.fxsj > today) {
                        if (n_day == null) {
                            n_day = it.fxsj
                        }
                        if (it.fxsj < n_day) {
                            n_day = it.fxsj
                        }
                    }
                }
                print(n_day)
                print("----")

                row.next_pay_time = n_day != null ? n_day : null
                print("next_pay_time:" + row.next_pay_time)
                row.next_pay_amount = investmentArchivesResourceService.getPayOnceAmount(it)
                res.add(row)
            }
            return [data: res, total: dc.count()]
        }
    }

    //todo:修改为新的数据返回方式
    @GET
    @Path('/nameLike')
    Response findByNameLike(@QueryParam('params') String htbn) {
        MyResponse.ok {

            def ia = Contract.findAllByHtbh("%" + htbn + "%")

            def jsonArray = []
            ia.each {
                def jso = [:]
                jso.put("value", it.htbh)
                jso.put("data", it.id)
                jsonArray.add(jso)
            }
            def data = [:]
            data.put("query", "Unit")
            data.put("suggestions", jsonArray)

            return data
        }
    }

    /**
     * 检测合同是否已经使用
     * @param num
     * @return
     */
    @GET
    @Path('/contractNumIsUse')
    Response contractNumIsUse(@QueryParam('num') String num) {
        MyResponse.ok {
            InvestmentArchives.findByContractNum(num)
        }
    }

    @GET
    @Path('/contractNumCanAdd')
    Response contractNumCanAdd(@QueryParam('num') String num) {
        MyResponse.ok {
            def ia = InvestmentArchives.findByContractNum(num)
            if (ia != null) {
                throw new Exception("合同编号已经使用了")
            }
            def c = Contract.findByHtbh(num)
            if (c == null) {
                throw new Exception("合同编号还没有登记")
            }
            c.properties
        }
    }
    /**
     * 获取后台算法产生的付息次数
     * @param date
     * @param qx
     * @param fxfs
     * @return
     * @QueryParam ( ' d a t e ' ) Date date,@QueryParam('fxfs') String qx,@QueryParam('fxfs') String fxfs
     */
    @POST
    @Path('/getPayTimes')
    Response getPayTimes(Map arg) {
        MyResponse.ok {
            String str = arg.date
            str = str.replace("T", " ")
            str = str.replace("Z", "")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            Date startDate = sdf.parse(str);
            return investmentArchivesResourceService.scfxsj(startDate, arg.qx, arg.fxfs.toUpperCase())
        }
    }

    /**
     * 返回指定合同编号的投资档案信息
     * @param contractNum
     * @return
     */
    @GET
    @Path('getByContractNum')
    Response getByContractNum(@QueryParam('contractNum') String contractNum) {
        MyResponse.ok {
            def result = [:]
            def iv = InvestmentArchives.findByContractNum(contractNum)
            if (iv) {
                result.putAt("id", iv.id)
//                print(InvestmentArchives.class.name)
//                result.putAt("class",InvestmentArchives.class.name)
                result.putAll(iv.properties)
                return iv
            }
            return null
        }
    }

    @GET
    @Path('/detail')
    Response detail(@QueryParam('id') Long id) {
        MyResponse.ok {
            def result = [:]
            def res = investmentArchivesResourceService.read(id)
            result.putAll(res.properties)
            result.ywtcs = []
            res.ywtcs.each {
                result.ywtcs.push(it.properties)
            }
            result.gltcs = []
            res.gltcs.each {
                result.gltcs.push(it.properties)
            }
            //附加付息信息
            result.paymentInfo = PaymentInfo.findAllByArchivesId(res.id)
            //退伙信息
            //续投信息
            result
        }
    }
}
