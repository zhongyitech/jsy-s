package com.jsy.bankPacket

/**
 * ���ʻ�ת���ر��ĸ�ʽ
 */
class Pack4004R {
    Pack4004 request
    String _ThirdVoucher
    //������ˮ��
    String _FrontLogNo
    //���ڿͻ�ת�˵ǼǺ��ڲ�ʶ��ͨ��ת�˽����ѯ���Է��ء����в����Ψһ��
    String _CstInnerFlowNo
    String _CcyCode
    String _OutAcctName
    String _OutAcctNo
    String _InAcctBankName
    String _InAcctNo
    String _InAcctName
    String _TranAmount
    // ���ڿ��б�־ 1������ת�ˣ�0������ת��
    int _UnionFlag
    String _Fee1
    String _Fee2
    //ת�˳ɹ������з��ش�Ʊ�ţ���ͬ������ϸ���صĴ�Ʊ�š�
    String _SOA_VOUCHER
    //ת�˳ɹ������з��ص���ˮ�š�
    String _hostFlowNo
    String _Mobile

    static constraints = {
        _Fee2 nullable: true
    }
}
