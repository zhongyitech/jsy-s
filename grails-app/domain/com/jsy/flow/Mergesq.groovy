package com.jsy.flow

import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund

/**
 * �ϲ������
 */
class Mergesq {
    //�µ���id
    String newContractNum
    //��������
    String fundName
    //��ͬ���
    String htbh
    //��ע
    String bz
    //������
    User sqr
    //���벿��
    String sqbm
    //��������
    Date sqrq = new Date()

    Date unionStartDate

    //ʵ�ʿ۳���Ϣ
    BigDecimal real_lx

    static constraints = {
    }
}
