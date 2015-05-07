package com.jsy.bankPacket

/**
 * 单笔划转返回报文格式
 */
class Pack4004R {
    Pack4004 request
    String _ThirdVoucher
    //银行流水号
    String _FrontLogNo
    //用于客户转账登记和内部识别，通过转账结果查询可以返回。银行不检查唯一性
    String _CstInnerFlowNo
    String _CcyCode
    String _OutAcctName
    String _OutAcctNo
    String _InAcctBankName
    String _InAcctNo
    String _InAcctName
    String _TranAmount
    // 行内跨行标志 1：行内转账，0：跨行转账
    int _UnionFlag
    String _Fee1
    String _Fee2
    //转账成功后，银行返回传票号，等同交易明细返回的传票号。
    String _SOA_VOUCHER
    //转账成功后，银行返回的流水号。
    String _hostFlowNo
    String _Mobile

    static constraints = {
        _Fee2 nullable: true
    }
}
