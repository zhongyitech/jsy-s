package com.jsy.flow

import com.jsy.archives.Contract
import com.jsy.fundObject.Fund

/**
 * ��ͬԤ�����
 * �洢�������������ʹ�õĺ�ͬ��� ��δ������
 * ��ȡ����������ʱ��Ҫ�Ӵ˱���ɾ��
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
