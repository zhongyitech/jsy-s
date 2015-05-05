package com.jsy.bankServices

/**
 * 银行业务代理服务类
 * Created by lioa on 2015/4/23.
 */
class BankProxyService {

    //
    public def QueryBalance(Map accounts) {
        def data = []
        for (int i = 0; i < 3; i) {
            MessageBody msg = new MessageBody("")
            data.add(BankPacket.CreateRequest("4001", msg, null).SendPacket {
                BankPacket pack ->
                    def result = [:]
                    new XmlParser().parseText(pack.messageBody.getResult()).children().each {
                        result.put(it.name(), it.value()[0])
                    }
                    result
            })
        }
        data
    }
}
