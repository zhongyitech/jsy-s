package com.jsy.utility

import com.jsy.archives.Contract
import com.jsy.archives.FilePackage
import com.jsy.archives.INVESTMENT_STATUS
import com.jsy.archives.InvestmentArchives

/**
 * 合同使用流程类
 * Created by lioa on 2015/4/21.
 */
enum ContractFlow {
    /**
     * 合同登记操作
     */
    Input(0),
    /**
     * 创建投资档案
     */
            CreateInvestment(1),
    /**
     * 档案入库
     */
            InputFilePackage(2)

    private int _setp

    ContractFlow(int _setp) {
        this._setp = _setp
    }

    public boolean ValidationNum(String num) {
        if (num == null || num.size() > 9)
            return new Exception("合同编号格式不正确")
        return Validation(Contract.findByHtbh(num))
    }

    public boolean Validation(Contract contract) {
        switch (_setp) {
        //合同登记操作
            case 0:
                if (contract != null) {
                    throw new Exception("合同编号:" + contract.htbh + " 已经登记过了.")
                }
                break
        //创建投资档案
            case 1:
                if (contract == null) {
                    throw new Exception("合同编号还没有登记")
                }
                if (InvestmentArchives.findByContractNum(contract.htbh) != null) {
                    throw new Exception("合同编号已经使用过了!")
                }
                break
        //档案入库
            case 2:
                def iv = InvestmentArchives.findByContractNum(contract.htbh)
                if (iv == null) {
                    throw new Exception("没有此合同编号的投资档案.!")
                }else{
                    if(iv.customer==null){
                        throw new Exception("投资档案还没有填写客户信息,不能入库.!")
                    }
                }
                if (FilePackage.findByContractNum(contract.htbh)) {
                    throw new Exception("此合同编号:" + contract.htbh + " 的投资档案已经入库了.!")
                }
                break
        }
        return true
    }
}