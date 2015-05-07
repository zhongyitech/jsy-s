package com.jsy.bankServices

import grails.converters.XML

/**
 * 银行业务代理服务类
 * Created by lioa on 2015/4/23.
 */
class BankProxyService {

    /**
     * 查询账户余额查询
     * @param accounts
     * @return
     */
    public def QueryBalance(Map accounts) {
        MessageBody msg = new MessageBody("")
        BankPacket.CreateRequest("4001", msg, null).SendPacket {
            BankPacket pack ->
                def result = [:]
                new XmlParser().parseText(pack.messageBody.getResult()).children().each {
                    result.put(it.name(), it.value()[0])
                }
                print(result)
                result
        }
    }

    /**
     * 查询账户历史交易明细
     * @param arg
     * @return
     */
    def TransatcionRecords(Map arg) {
        def sb = new StringBuilder()
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
        sb.append("<Result>")
        sb.append("<AcctNo>" + arg.AcctNo + "</AcctNo>")
        sb.append("<CcyCode>" + arg.CcyCode + "</CcyCode>")
        sb.append("<BeginDate>" + arg.BeginDate + "</BeginDate>")
        sb.append("<EndDate>" + arg.EndDate + "</EndDate>")
        sb.append("<PageNo>" + arg.PageNo + "</PageNo>")
        sb.append("<Reserve>" + arg.Reserve + "</Reserve>")
        sb.append("</Result>")
        BankPacket.CreateRequest("4013", new MessageBody(sb.toString()), null).SendPacket {
            BankPacket pack ->
                def xmlp = new XmlParser().parseText(pack.messageBody.getResult())
                def result = [:]
                xmlp.children().each {
                    if (it.value().size() == 0) {
                        result.put(it.name(), it.value()[0])
                    }
                }
                def list = []
                xmlp.getByName("list").each {
                    def obj = [:]
                    it.value().each {
                        obj.put(it.name(), it.value()[0])
                    }
                    list.add(obj)
                }
                result.put("list", list)
                print(result)
                result
        }
    }
}
