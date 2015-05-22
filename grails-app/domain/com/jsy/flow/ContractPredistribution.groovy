package com.jsy.flow

import com.jsy.archives.Contract
import com.jsy.fundObject.Fund

/**
 * 合同预分配表：
 * 存储分配给特殊申请使用的合同编号 （未审批）
 * 在取消特殊申请时需要从此表中删除
 */
class ContractPredistribution {

    String htbh
    Fund fund
    String fundName
    String guid
    static constraints = {
        fund nullable: true
    }

    static def addRow(String htbh, String queryKey) {
        def fund = Contract.findByHtbh(htbh)?.fund
        return new ContractPredistribution(htbh: htbh, fund: fund, fundName: fund != null ? fund.fundName : "", guid: queryKey)
    }
}
