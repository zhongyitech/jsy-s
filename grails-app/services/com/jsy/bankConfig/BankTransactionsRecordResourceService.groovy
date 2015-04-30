package com.jsy.bankConfig

import com.jsy.utility.BankOrderUntil
import grails.transaction.Transactional
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
            it.manageType = 2
            def summary = Summary.findByRemarkLikeAndAccountName("%" + it.summary + "%", it.account)
            if (summary != null) {
                def bankAccount = BankAccount.findByAccount(it.account)
                if (bankAccount != null && bankAccount.companyInformation != null) {
                    def subject = SummaryToFund.findBySumNameAndCompanyAndBorrow(summary.summary, bankAccount.account, it.borrowAndLend)
                    if(subject==null){
                        print(summary.summary +","+bankAccount.account+","+it.borrowAndLend)
                        return
                    }
                    def orderEntry = new BankOrderEntry()
                    def arg=[bank:bankAccount,company:bankAccount.companyInformation,record:it]
                    orderEntry.summary = summaryFormat(summary.summary,arg)
                    def subject2 = subjectFormat(subject,arg)
                    orderEntry.subjectName = subject != null ? subject.subject + "-" + subject2 : ""
                    orderEntry.company = bankAccount.companyInformation.companyName
                    orderEntry.transaction = it.transactionsCode
                    orderEntry.dealDate=it.dealDate
                    if (it.borrowAndLend) {
                        orderEntry.borrowAmount = (it.actionAmount)
                    } else {
                        orderEntry.lendAmount = it.actionAmount
                    }
                    orderEntry.save(failOnError: true)
                    it.manageType = 1
                    //设置成功处理的标志
                    it.managed = true
                    entryList.push(orderEntry)
                }
            }
            it.processedDate = new Date()
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

    String subjectFormat(SummaryToFund stf, def arg) {
        print("subject:" + stf.subjectLevel2)
        if (stf.subjectLevel2 == null) return ""
        return BankOrderUntil.Instance().GetFormatValue(stf.subjectLevel2, arg)
    }
    String summaryFormat(String summary, def arg) {
//        print("subject:" + stf.subjectLevel2)
        if (summary == null) return ""
        return BankOrderUntil.Instance().GetFormatValue(summary, arg)
    }
}
