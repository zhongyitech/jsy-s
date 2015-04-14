package com.jsy.bankConfig

import com.jsy.fundObject.FundCompanyInformation
import com.jsy.utility.BankOrderUntil
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class BankTransactionsRecordResourceService {
    static final int MANAGE_TYPE_Processed = 1
    //新的记录
    static final int MANAGE_TYPE_NEW = 0
    //弃用的记录
    static final int MANAGE_TYPE_DEL = 3

    def create(BankTransactionsRecord dto) {
        dto.save()
    }

    def read(id) {
        def obj = BankTransactionsRecord.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(BankTransactionsRecord.class, id)
        }
        obj
    }

    def readAll() {
        BankTransactionsRecord.findAll()
    }

    def update(BankTransactionsRecord dto) {
        def obj = BankTransactionsRecord.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(BankTransactionsRecord.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = BankTransactionsRecord.get(id)
        if (obj) {
            obj.delete()
        }
    }

    /**
     * 处理银行流水记录,生成记账凭证单
     */
    def ProcessTransactionData() {
        def list = BankTransactionsRecord.findAllByManagedAndSummaryIsNotNull(false)
        def entryList = []
        list.each {
            it.manageType=2
            def summary = Summary.findByRemarkLikeAndAccountName("%" + it.summary + "%", it.account)
            if (summary != null) {
                def bankAccount = BankAccount.findByAccount(it.account)
                if (bankAccount != null && bankAccount.companyInformation != null) {
                    def subject = SummaryToFund.findBySumNameAndCompanyAndBorrow(summary.summary, bankAccount.account, it.borrowAndLend)
                    def orderEntry = new BankOrderEntry()
                    orderEntry.summary = summary.summary
                    //subjectFormat(subject,bankAccount)
                    orderEntry.subjectName = subject != null ? subject.subject + "-" + (subject.subjectLevel2!=null ? subject.subjectLevel2 : "")  : ""
                    orderEntry.company=bankAccount.companyInformation.companyName

                    orderEntry.transaction = it.transactionsCode
                    if (it.borrowAndLend) {
                        orderEntry.borrowAmount = (it.actionAmount)
                    } else {
                        orderEntry.lendAmount = it.actionAmount
                    }
                    orderEntry.save(failOnError: true)
                    it.manageType=1
                    entryList.push(orderEntry)
                }
            }
            it.managed=true
            it.processedDate=new Date()
            it.save(failOnError: true)
        }
        return entryList
    }
    /**
     * 根据规则组合摘要数据
     * @param summary
     * @return
     */
    def formatSummary(Summary summary) {
        return summary.summary
    }


    void subjectFormat(SummaryToFund stf,BankAccount baccount){

        BankOrderUntil.Instance().GetFormatValue(stf.subjectLevel2,baccount)
    }
}
