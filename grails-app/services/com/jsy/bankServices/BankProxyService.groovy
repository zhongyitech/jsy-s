package com.jsy.bankServices

import grails.converters.XML

/**
 * 银行业务代理服务类
 * Created by lioa on 2015/4/23.
 */
class BankProxyService {
    static Map ErrorCode = [
            "000000": [value: 0, "dest": "正常"]
    ]

    /**
     * 查询账户余额查询
     * @param accounts
     * @return
     */
    public def QueryBalance(Map accounts) {
        MessageBody msg = new MessageBody("")
        def result = [:]
        BankPacket.CreateRequest("4001", msg, null).SendPacket {
            BankPacket pack ->
                new XmlParser().parseText(pack.messageBody.getResult()).children().each {
                    result.put(it.name(), it.value()[0])
                }
                pack
        }
        print(result)
        result
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

    /**
     *  系统状态探测
     * @return 服务状态是否可用
     */
    boolean CheckServerStatus() {
        try {
            MessageBody msg = new MessageBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Result></Result>")
            boolean status = false
            BankPacket.CreateRequest("S001", msg, null).SendPacket { BankPacket pack ->
                def resultXml = new XmlParser().parseText(pack.messageBody.getResult())
                def desc = resultXml.get("Desc")[0].value()[0]
                if (ErrorCode.containsKey(desc)) {
                    status = (ErrorCode[desc].value == 0)
                }
                pack
            }
            return status
        }
        catch (EMPException mEx) {
            throw mEx
        }
        catch (Exception e) {
            throw e
        }
    }
}
