package com.jsy.utility

import com.jsy.archives.FilePackage
import com.jsy.archives.INVESTMENT_STATUS
import com.jsy.archives.InvestmentArchives

/**
 * Ͷ�ʵ������̹��ܼ��
 * Created by lioa on 2015/4/23.
 */
enum InvestmentFlow {
    Create(0),Update(1),Deleted(2)

    private int _setp

    InvestmentFlow(int setp) {
        this._setp = setp
    }

    /**
     * ���Ͷ�ʵ����Ƿ��ܽ���Щ����
     * @param ia Ͷ�ʵ���
     * @return �׳�������쳣����
     */
    public boolean Validation(InvestmentArchives ia) {
        if(ia==null){
            throw new MyException("Ͷ�ʵ���Ϊ��,������ֵ��")
        }
        switch (_setp){
            //create
            case 0:
                //��ͬ����Ƿ��Ѿ�ʹ�ù���
                ContractFlow.CreateInvestment.ValidationNum(ia.contractNum)
                break
            //update
            case 1:
                if(INVESTMENT_STATUS.BackUp.eq(ia.status)){
                    throw new MyException('Ͷ�ʵ���״̬��\"�浵\"״���ܽ����޸�.')
                }
                if(FilePackage.findByContractNum(ia.contractNum)){
                    throw new MyException("Ͷ�ʵ����Ѿ���⣬�����޸� ��")
                }
                break
            //deleted
            case  2:
                break
            default:
                break
        }
        return  true
    }
}
