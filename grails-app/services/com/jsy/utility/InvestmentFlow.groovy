package com.jsy.utility

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
        this._setp = _setp
    }

    /**
     * 检测投资档案是否能进行些操作
     * @param ia 投资档案
     * @return 抛出具体的异常错误
     */
    public boolean Validation(InvestmentArchives ia) {
        switch (_setp){
            //create
            case 0:

                break
            //update
            case 1:
                if(INVESTMENT_STATUS.BackUp.eq(ia.status)){
                    throw new MyException("投资档案已经归档不能进行修改.")
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
