package com.jsy.archives

class IAOutput {

    String oid

    //认购人 Customer.name
    String customer

    //基金名称 Fund.name
    String fundname

    //档案编号
    String markNum

    //合同编号
    String contractNum

    //认购日期
    Date rgrq

    //投资期限
    String tzqx

    //认购金额
    BigDecimal sjtzje

    //理财经理 User.name
    String ywjl

    //地区 Customer
    String country

    //年化收益率
    double  nhsyl

    //付息方式
    String fxfs

    //到期日期
    Date dqrq

    //委托情况
    int dazt

    //已付利息 Payment.yfk(dflx=lx)
    BigDecimal bj

    //已付本金 Payment.yfk(dflx=bj)
    BigDecimal lx



    static constraints = {
    }
    public IAOutput(InvestmentArchives dto){
        oid = dto.id.toString()
        customer = dto.customer.name
        fundname = dto.fund.fundName
        markNum = dto.markNum
        contractNum = dto.contractNum
        rgrq = dto.rgrq
        tzqx = dto.tzqx
        sjtzje = dto.sjtzje
        ywjl = dto.ywjl.chainName
        country = dto.customer.country
        nhsyl = dto.nhsyl
        fxfs = dto.fxfs
        dqrq = dto.dqrq
        dazt = dto.dazt
        bj = 0
        Payment.findAllByContractNumAndDflx(dto.contractNum, "lx").each {
            bj = bj + it.yfk
        }
        lx = 0
        Payment.findAllByContractNumAndDflx(dto.contractNum, "bj").each {
            lx = lx + it.yfk
        }
    }
}
