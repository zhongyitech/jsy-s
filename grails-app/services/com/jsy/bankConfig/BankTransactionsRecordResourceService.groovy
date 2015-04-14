package com.jsy.bankConfig

import com.jsy.fundObject.FundCompanyInformation
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
//        def data = BankTransactionsRecord.findAllByManaged(false)
//        if (data.size() == 0) return
        def list = BankTransactionsRecord.findAllByManagedAndSummaryIsNotNull(false)
        def entryList = []
        list.each {
            def orderEntry = new BankOrderEntry()
            //
            def summary = Summary.findByRemarkLikeAndAccountName("%" + it.summary + "%", it.accountName)
            if (summary == null) {
                return
            }
            def bankaccount=BankAccount.findByAccount(it.account)
            def company = FundCompanyInformation.get()
            if (company == null) {
                return
            }
            def subject = SummaryToFund.find {
                eq("borrowAndLend", it.borrowAndLend)
                eq("subject", formatSummary(summary))
                eq("fund", company.companyName)
            }
            orderEntry.summary = summary
            orderEntry.subjectName = subject.subject
            orderEntry.transaction = it.transactionsCode
            if (it.borrowAndLend) {
                orderEntry.borrowAmount = it.actionAmount
            } else {
                orderEntry.lendAmount = it.actionAmount
            }
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
}
