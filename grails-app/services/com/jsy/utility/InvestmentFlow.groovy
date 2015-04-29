package com.jsy.utility

import com.jsy.archives.FilePackage
import com.jsy.archives.INVESTMENT_STATUS
import com.jsy.archives.InvestmentArchives

/**
 * 投资档案流程功能检测
 * Created by lioa on 2015/4/23.
 */
enum InvestmentFlow {
    Create(0),Update(1),Deleted(2)

    private int _setp

    InvestmentFlow(int setp) {
        this._setp = setp
    }

    /**
     * 检测投资档案是否能进行些操作
     * @param ia 投资档案
     * @return 抛出具体的异常错误
     */
    public boolean Validation(InvestmentArchives ia) {
        if(ia==null){
            throw new MyException("投资档案为空,检测参数值。")
        }
        switch (_setp){
            //create
            case 0:
                //合同编号是否已经使用过了
                ContractFlow.CreateInvestment.ValidationNum(ia.contractNum)
                break
            //update
            case 1:
                if(INVESTMENT_STATUS.BackUp.eq(ia.status)){
                    throw new MyException('投资档案状态是\"存档\"状不能进行修改.')
                }
                if(FilePackage.findByContractNum(ia.contractNum)){
                    throw new MyException("投资档案已经入库，不能修改 ！")
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
