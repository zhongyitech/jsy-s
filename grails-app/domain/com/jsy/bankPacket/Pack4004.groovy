package com.jsy.bankPacket

/**
 * 企业单笔资金划转  交易码(4004)
 */
class Pack4004 {
    //银行是否已经成功处理
    boolean processed = false
    //备注
    String bz = ""

    String _ThirdVoucher
    //用于客户转账登记和内部识别，通过转账结果查询可以返回。银行不检查唯一性
    String _CstInnerFlowNo
    // "RMB" char(3)
    String _CcyCode
    String _OutAcctNo
    String _OutAcctName
    String _OutAcctAddr
    //跨行转账建议必输。 为人行登记在册的商业银行号
    String _InAcctBankNode
    String _InAcctRecCode
    String _InAcctNo
    String _InAcctName
    /*建议格式：xxx银行*/
    String _InAcctBankName
    /*建议跨行转账输入；对照码参考“附录-省对照表”；也可输入“附录-省对照表”中的省名称。*/
    String _InAcctProvinceCode
    String _InAcctCityName
    //转出金额 以 元 为单位
    BigDecimal _TranAmount
    String _AmountCode
    String _UseEx
    String _UnionFlag
    /*‘1’—大额 ，等同Y ‘ 2 ’—小额”等同N    Y ： 加急 N：普通   S：特急 默认为N */
    String _SysFlag = "N"
    //“1”—同城   “2”—异地
    int _AddrFlag
    String _MainAcctNo
    String _Mobile
    //1：上海登记公司－资金清算业务 2 ： 上海登记公司－网下发行电子化业务 3 ： 深圳登记公司－网上业务 4 ： 深圳登记公司－网下IPO业务
    int _zdJType
    String _zdZType
    String _zdBAcc
    String _ProxyPayName
    String _ProxyPayAcc
    String _ProxyPayBankName
    String _InIDType
    String _InIDNo
    String _BFJTranType
    String _BFJBizType

    static constraints = {
        _CstInnerFlowNo nullable: true
        _OutAcctAddr nullable: true
        _InAcctBankNode nullable: true
        _InAcctRecCode nullable: true
        _InAcctProvinceCode nullable: true
        _InAcctCityName nullable: true
        _AmountCode nullable: true
        _UseEx nullable: true
        _SysFlag nullable: true
        _MainAcctNo nullable: true
        _Mobile nullable: true
        _zdJType nullable: true
        _zdZType nullable: true
        _zdBAcc nullable: true
        _ProxyPayAcc nullable: true
        _ProxyPayBankName nullable: true
        _ProxyPayName nullable: true
        _InIDNo nullable: true
        _InIDType nullable: true
        _BFJBizType nullable: true
        _BFJTranType nullable: true
    }
}
