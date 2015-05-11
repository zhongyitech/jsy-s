package com.jsy.archives

import com.jsy.bankPacket.Pack4004
import com.jsy.bankServices.BankProxyService
import com.jsy.fundObject.Finfo
import com.jsy.fundObject.Fund
import com.jsy.fundObject.FundCompanyInformation
import com.jsy.system.Company
import com.jsy.system.TypeConfig
import com.jsy.utility.MyException
import com.jsy.utility.PAYMENT_STATUS
import com.jsy.utility.UtilityString
import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class PaymentResourceService {
    BankProxyService bankProxyService
    PaymentInfoResourceService paymentInfoResourceService
    UserCommisionResourceService userCommisionResourceService
    /**
     * 调用银行接口处理兑付单
     * @param dto
     * @param i
     * @return
     */
    //更新状态到3
    def updatePayment(Payment dto, int i) {
        //设置金额转化的格式
        java.text.DecimalFormat df = new java.text.DecimalFormat("00000000000000.00");
        def thirdVoucher = new Date().format("yyyymmddHHmmSS") + "P4004"

        //todo:账号不是14位吗? 为什么要32位
        def pay4004 = new Pack4004(
                _ThirdVoucher: thirdVoucher,
                _InAcctNo: UtilityString.RequestFormat(dto.zh, 32),
                _InAcctName: UtilityString.RequestFormat(dto.customerName, 60),
                _InAcctBankName: UtilityString.RequestFormat(dto.khh, 60),
                _TranAmount: dto.fpe
                , _CcyCode: "RMB")

        def fund = Fund.findByFundName(dto.fundName.trim())
        if (fund == null || fund.funcCompany == null) throw new Exception("兑付单中的基金名称没有对应的基金对象/或合伙企业!")
        def bankAccounts = fund.funcCompany.bankAccount.findAll {
            it.purpose == TypeConfig.findByTypeAndMapName(7, "兑付")
        }
        if (bankAccounts.size() == 0) throw new Exception(dto.fundName + "此合伙企业没有默认的兑付账户!")
        ////new TypeConfig(type: 7, mapName: "兑付", mapValue: 1).save(failOnError: true)
        def bankaccount = bankAccounts.size() == 1 ? bankAccounts.first() : bankAccounts.find {
            it.defaultAccount
        }
        //多个总值账户,但是没有设置默认值时选取第一个
        if (bankaccount == null && bankAccounts.size() > 1) {
            bankAccount = bankAccounts.first()
        }
        //设置付款人信息
        pay4004._OutAcctNo = bankaccount.account
        pay4004._OutAcctName = bankaccount.accountName
        pay4004._UnionFlag = "1"
        //todo:调用转账接口
        def resultData = bankProxyService.TransferSing(pay4004)

        if (resultData != null && resultData.containsKey("FrontLogNo")) {
            dto.frontLogNo = resultData["FrontLogNo"]
            dto.fee1 = resultData["Fee1"]
        }
        dto.status = PAYMENT_STATUS.Paying

        //尝试进行一次查询 失败直接跳过
        try {
            def queryResult = bankProxyService.TransferSingleQuery(pay4004._ThirdVoucher, dto.frontLogNo)
            dto.payStatus = queryResult.code + ":" + queryResult.msg
            //查询到支付成功就设置为"已支付"
            if (queryResult.success) {
                dto.status = PAYMENT_STATUS.PaySuccess
                dto.yfsj = new Date()
            }
        } catch (Exception ex) {
            //不处理 尝试性操作,后续会有任务再查询交易结果
        }
        dto.lastUpdated = new Date()
        pay4004.save(failOnError: true)
        dto.save(failOnError: true)
        //返回银行的接收信息
        resultData
    }

    def create(Payment dto) {
        dto.save()
    }

    def read(id) {
        def obj = Payment.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Payment.class, id)
        }
        obj
    }

    def readAll() {
        Payment.findAll()
    }

    def getPayments(Finfo finfo, String type) {
        return Payment.findAllByDflxAndStatusInListAndCustomerNameLikeAndZfsjBetween(type, [0, 1], "%" + (finfo.keyword == null ? "" : finfo.keyword) + "%", finfo.startsaledate1, finfo.startsaledate2, [max: finfo.pagesize, offset: finfo.startposition, sort: "status"])
    }

    def getPaymentsTotal(Finfo finfo, String type) {
        return Payment.findAllByDflxAndStatusInListAndCustomerNameLikeAndZfsjBetween(type, [0, 1], "%" + (finfo.keyword == null ? "" : finfo.keyword) + "%", finfo.startsaledate1, finfo.startsaledate2).size()
    }

    def getCommissions(Finfo finfo, String type) {
        return Payment.findAllByDflxAndStatusInListAndCustomerNameLikeAndZfsjBetween(type, [0, 1], "%" + (finfo.keyword == null ? "" : finfo.keyword) + "%", finfo.startsaledate1, finfo.startsaledate2, [max: finfo.pagesize, offset: finfo.startposition, sort: "status"])
    }

    def getCommissionsTotal(Finfo finfo, String type) {
        return Payment.findAllByDflxAndStatusInListAndCustomerNameLikeAndZfsjBetween(type, [0, 1], "%" + (finfo.keyword == null ? "" : finfo.keyword) + "%", finfo.startsaledate1, finfo.startsaledate2).size()
    }

    def update(Payment dto) {
        def obj = Payment.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Payment.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Payment.get(id)
        if (obj) {
            obj.delete()
        }
    }

    /**
     * 设置些兑付单已经完结
     * 1.设置关联数据的状态
     */
    def setPaySuccess(Payment payment) {
        String dflx = payment.dflx.trim()
        switch (dflx) {
            case "lx":  //利息支付
            case "bj":  //本金支付
                def target = PaymentInfo.get(payment.infoId)
                if (target != null) {
                    paymentInfoResourceService.setSuccess(target)
                    target.save(failOnError: true)
                }
                break
            case "yw":  //业务提成支付
                def target = UserCommision.get(payment.infoId)
                if (target != null) {
                    userCommisionResourceService.setSuccess(target,payment)
                    target.save(failOnError: true)
                }
                break
            case "gl":  //管理提成支付
                def target = UserCommision.get(payment.infoId)
                if (target != null) {
                    userCommisionResourceService.setSuccess(target, payment)
                    target.save(failOnError: true)
                }
                break
            default:
                throw MyException("兑付单的类型错误!没有些类型的兑付单:" + dflx)
                break
        }
        payment.status = PAYMENT_STATUS.PaySuccess
        payment.save(failOnError: true)
    }
}
