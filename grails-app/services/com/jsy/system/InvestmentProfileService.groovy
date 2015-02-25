package com.jsy.system

import grails.transaction.Transactional

@Transactional
class InvestmentProfileService {
/**
 * 到期转投
 *
 * @param Investment_NO 原投资档案编号
 * @param amount        追加的投资金额，不增加为0
 */
    def maturityTransferInvestment(def investmentNO,def amount){
        //todo:
    }
}
