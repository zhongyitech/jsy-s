package com.jsy.utility

import com.jsy.archives.INVESTMENT_STATUS
import com.jsy.archives.InvestmentArchives

/**
 * 特殊申请单的流程检测
 * Created by lioa on 2015/4/21.
 */
enum SpecialFlow {
    Create(0), Edit(1), Accept(2), Cancel(3)
    private int _step = 0

    SpecialFlow(int _step) {
        this._step = _step
    }
    /**
     * 特殊申请的操作流程功能性判断
     * @param iv
     * @return
     */
    public boolean Validation(InvestmentArchives iv) {
        if (iv == null) {
            throw new MyException("合同编号不正确!", "htbh")
        }
        switch (_step) {
            case 0:
                if (iv.customer == null) {
                    throw new MyException("投资档案的客户信息还没有完善,不能做特殊申请操作..")
                }
                if (iv.dazt != 0) {
                    throw new MyException("投资档案已经有特殊申请操作了!")
                }
                print(iv.status)
                if (!INVESTMENT_STATUS.Normal.eq(iv.status)) {
                    throw new MyException("投资档案还没有入档,不能做特殊申请操作.")
                }
                break;
            case 1:
                if (iv.dazt != 0) {

                }
                break;
            case 2:
                break
            default:
                break
        }
        return true
    }
}