package com.jsy.archives

import com.jsy.bankPacket.Pack4004
import com.jsy.fundObject.Finfo
import com.jsy.fundObject.Fund
import com.jsy.fundObject.FundCompanyInformation
import com.jsy.system.Company
import com.jsy.system.TypeConfig
import com.jsy.utility.UtilityString
import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class PaymentResourceService {

    /**
     * 调用银行接口处理兑付单
     * @param dto
     * @param i
     * @return
     */
    //更新状态到3
    def updatePayment(Payment dto, int i) {
        try {
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
            if (fund == null || fund.funcCompany) throw new Exception("兑付单中的基金名称没有对应的基金对象/或合伙企业!")
            def bankAccounts = fund.funcCompany.bankAccount.findAll {
                it.purpose == TypeConfig.findByTypeAndMapName(1, "兑付")
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
            pay4004._UnionFlag="1"

            pay4004.save(failOnError: true)

            dto.status = 1
            dto.save(failOnError: true)
        } catch (Exception ex) {

        }
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
}
