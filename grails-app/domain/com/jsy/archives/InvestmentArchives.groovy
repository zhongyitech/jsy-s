package com.jsy.archives

import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.customerObject.CustomerCommision
import com.jsy.fundObject.Fund
import com.jsy.system.TypeConfig
import com.jsy.system.UploadFile
import com.jsy.utility.CreateNumberService
import com.jsy.utility.DateUtility
import com.jsy.utility.GetYieldService

import java.text.SimpleDateFormat

/**
 * 投资档案
 */
class InvestmentArchives {

    //投资档案编号，自动生成
    String markNum

    //客户档案(客户的复件资料）
    CustomerArchives customerArchive

    //客户
    Customer customer

    //档案信息
    //档案编号
    String archiveNum
    //来源（档案来源：转投(来源编号)、新增则为0）
    int archiveFrom = 0
    //合同编号
    String contractNum
    //基金名称
    Fund fund
    //投资金额
    BigDecimal tzje
    //投资期限
    String tzqx
    //认购日期
    Date rgrq
    //到期日期
    Date dqrq
    //付息方式
    String fxfs
    //年化收益率
    double nhsyl
    //合同状态
    TypeConfig htzt
    //备注
    String dabz

    //提成信息
    //部门经理
    User bmjl
    //业务经理
    User ywjl
    //部门
    String bm
    //管理提成
    double gltc
    //业务提成
    double ywtc
    //备注说明
    String description

    //投资确认书
    //客户名
    String username
    //基金名
    String fundName
    //投资金额
    BigDecimal sjtzje
    //投资期限
    String qx
    //投资预期收益率
    String yqsyl
    //计息开始日期
    Date startDate
    //计息结束日期
    Date endDate
    //第一次付息时间
    Date fxsj1
    //第二次付息时间
    Date fxsj2
    //第三次付息时间
    Date fxsj3
    //第四次付息时间
    Date fxsj4
    //全部付完了或者转投结束，不在付款
    //是否已经生成过兑付数据(在兑付查询中是否已经有记录)
    boolean stopPay = false
    //打印次数
    int dycs = 0
    //最近打印时间
    Date zjdysj

    //本金
    BigDecimal bj

    //业务和客户收益分配
    //附件
    static hasMany = [ywtcs: UserCommision, gltcs: UserCommision, customerCommision: CustomerCommision, uploadFiles: UploadFile, payTimes: PayTime]

    //档案状态 //0是正常的//1到期转投2未到期转投3基金续投申请4退伙申请
    int dazt = 0
    //档案状态 //0新建，1正常，2存档
    int status = 0

    //能否兑付，要入库后
    boolean yrk = false

    /**
     * 处理新建数据时的必要处理逻辑
     * @return
     */
    def beforeInsert() {
        //新的投资档案本金与投资相等
        bj = tzje
        /*  自动计算到期日期，不使用传入的数据 */
        def qx = Double.parseDouble(this.tzqx.substring(0, this.tzqx.length() - 1))
        this.dqrq = DateUtility.addMonth(this.rgrq, (int) (qx * 12))
        //设置提成数据
        GetYieldService.restSetTc(this)
        //生成档案号
        StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("I"))
        println("archiveNum " + CreateNumberService.getRandomNumber(new StringBuffer(former)))
        this.archiveNum = CreateNumberService.getRandomNumber(new StringBuffer(former))
    }

    static constraints = {
        archiveNum unique: true
        contractNum unique: true
        bm nullable: true
        description nullable: true
        bmjl nullable: true
        ywjl nullable: true
        dabz nullable: true
        username nullable: true
        fundName nullable: true
        sjtzje nullable: true
        qx nullable: true
        yqsyl nullable: true
        startDate nullable: true
        endDate nullable: true
        fxsj1 nullable: true
        fxsj2 nullable: true
        fxsj3 nullable: true
        fxsj4 nullable: true

        customer nullable: true
        customerArchive nullable: true

        zjdysj nullable: true

        bj nullable: true
    }
}
