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
            if (it.summary == "") {
                it.processDescript = "无备注"
                it.managed = true
                it.save(failOnError: true)
                //设置为已处理但处理失败
                return
            }
            println("id:$it.id -> summary:$it.summary")
            def summary = Summary.findByRemarkLikeAndAccountName("%" + it.summary + "%", it.account)
            if (summary != null) {
                def bankAccount = BankAccount.findByAccount(it.account)
                if (bankAccount != null && bankAccount.companyInformation != null) {
                    //以银行账号和摘要 匹配出科目数据 ；此数据应该有两条（借方和贷方）
                    def subjects = SummaryToFund.findAllBySumNameAndCompany(summary.summary, bankAccount.account)
                    if (subjects == null && subjects.size() < 2) {
                        //科目设置不匹配
                        print(summary.summary + "," + bankAccount.account + "," + it.borrowAndLend)
                        return
                    }
                    def arg = [bank: bankAccount, company: bankAccount.companyInformation, record: it]
                    //循环处理借方科目和货方科目
                    subjects.each { SummaryToFund subject ->
                        def orderEntry = new BankOrderEntry()
                        //摘要：对占位符进行替换操作
                        orderEntry.summary = summaryFormat(summary.summary, arg)
                        //设置二级科目  占位符替换
                        def subject2 = subject.subjectLevel2 != null ? subjectFormatU(subject.subjectLevel2, arg) : ""
                        //设置三级科目  占位符替换
                        def subject3 = subject.subjectLevel3 != null ? subjectFormatU(subject.subjectLevel3, arg) : ""
                        //组合科目名称中间用“-”分隔
                        orderEntry.subjectName = (subject.subject + ((subject2 != "") ? "-" + subject2 : "") + ((subject3 != "") ? "-" + subject3 : "")).trim()
                        //设置账户名称
                        //TODO:加一个从账套对应表中获取数据的方式
                        orderEntry.company = getAccountName(bankAccount.companyInformation.companyName)
                        orderEntry.transaction = it.transactionsCode
                        orderEntry.dealDate = it.dealDate
                        if (subject.borrow) {
                            orderEntry.borrowAmount = (it.actionAmount)
                        } else {
                            orderEntry.lendAmount = it.actionAmount
                        }
                        orderEntry.save(failOnError: true)
                        entryList.push(orderEntry)
                    }
                    it.manageType = 1
                    //设置成功处理的标志
                    it.managed = true
                    it.processDescript = "已转换凭证"
                }
            } else {
                it.processDescript = "无关键字匹配"
            }
            //不管是否处理都更新处理时间
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

    String subjectFormatU(String subject, arg) {
        return BankOrderUntil.Instance().GetFormatValue(subject, arg)
    }

    String getAccountName(String commpany) {
        return commpany;
    }

    String summaryFormat(String summary, def arg) {
//        print("subject:" + stf.subjectLevel2)
        if (summary == null) return ""
        return BankOrderUntil.Instance().GetFormatValue(summary, arg)
    }
}
