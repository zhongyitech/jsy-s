package com.jsy.bankPacket

/**
 * ��ҵ�����ʽ�ת  ������(4004)
 */
class Pack4004 {
    //�����Ƿ��Ѿ��ɹ�����
    boolean processed = false
    //��ע
    String bz = ""

    String _ThirdVoucher
    //���ڿͻ�ת�˵ǼǺ��ڲ�ʶ��ͨ��ת�˽����ѯ���Է��ء����в����Ψһ��
    String _CstInnerFlowNo
    // "RMB" char(3)
    String _CcyCode
    String _OutAcctNo
    String _OutAcctName
    String _OutAcctAddr
    //����ת�˽�����䡣 Ϊ���еǼ��ڲ����ҵ���к�
    String _InAcctBankNode
    String _InAcctRecCode
    String _InAcctNo
    String _InAcctName
    /*�����ʽ��xxx����*/
    String _InAcctBankName
    /*�������ת�����룻������ο�����¼-ʡ���ձ���Ҳ�����롰��¼-ʡ���ձ��е�ʡ���ơ�*/
    String _InAcctProvinceCode
    String _InAcctCityName
    //ת����� �� Ԫ Ϊ��λ
    BigDecimal _TranAmount
    String _AmountCode
    String _UseEx
    String _UnionFlag
    /*��1������� ����ͬY �� 2 ����С���ͬN    Y �� �Ӽ� N����ͨ   S���ؼ� Ĭ��ΪN */
    String _SysFlag = "N"
    //��1����ͬ��   ��2�������
    int _AddrFlag
    String _MainAcctNo
    String _Mobile
    //1���Ϻ��Ǽǹ�˾���ʽ�����ҵ�� 2 �� �Ϻ��Ǽǹ�˾�����·��е��ӻ�ҵ�� 3 �� ���ڵǼǹ�˾������ҵ�� 4 �� ���ڵǼǹ�˾������IPOҵ��
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
