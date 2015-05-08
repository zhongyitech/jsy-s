package com.jsy.bankServices

import com.jsy.bankPacket.Pack4004
import com.jsy.utility.UtilityString
import grails.converters.XML

/**
 * 银行业务代理服务类
 * Created by lioa on 2015/4/23.
 */
class BankProxyService {

    /**
     * 查询账户余额查询 4001
     * @param arg [Account,CcyCode ]
     * @return
     */
    public def QueryBalance(Map arg) {
        def sb = new StringBuilder()
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
        sb.append("<Result>")
        sb.append("<Account>" + UtilityString.RequestFormat(arg.Account, 14) + "</Account>")
        sb.append("<CcyCode>" + UtilityString.RequestFormat(arg.CcyCode, 3) + "</CcyCode>")
        sb.append("</Result>")
        MessageBody msg = new MessageBody(sb.toString())
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
     * 查询账户历史交易明细 4013
     * @param arg [AcctNo,CcyCode,BeginDate,EndDate,PageNo,PageSize,Reserve]
     * @return
     */
    def TransatcionRecords(Map arg) {
        def sb = new StringBuilder()
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
        sb.append("<Result>")
        sb.append("<AcctNo>" + UtilityString.RequestFormat(arg.AcctNo, 14) + "</AcctNo>")
        sb.append("<CcyCode>" + UtilityString.RequestFormat(arg.CcyCode, 3) + "</CcyCode>")
        sb.append("<BeginDate>" + UtilityString.RequestFormat(arg.BeginDate, 8) + "</BeginDate>")
        sb.append("<EndDate>" + UtilityString.RequestFormat(arg.EndDate, 8) + "</EndDate>")
        sb.append("<PageNo>" + UtilityString.RequestFormat(arg.PageNo, 6) + "</PageNo>")
        sb.append("<PageSize>" + UtilityString.RequestFormat(arg.PageSize, 6) + "</PageSize>")
        sb.append("<Reserve>" + UtilityString.RequestFormat(arg.Reserve, 120) + "</Reserve>")
        sb.append("</Result>")
        def result = [:]

        BankPacket.CreateRequest("4013", new MessageBody(sb.toString()), null).SendPacket {
            BankPacket pack ->
                def xmlp = new XmlParser().parseText(pack.messageBody.getResult())
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
                pack
        }
        result
    }

    /**
     *  系统状态探测 S001
     * @return 服务状态是否可用
     */
    boolean CheckServerStatus() {
        try {
            MessageBody msg = new MessageBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Result></Result>")
            boolean status = false
            BankPacket.CreateRequest("S001", msg, null).SendPacket { BankPacket pack ->
                def resultXml = new XmlParser().parseText(pack.messageBody.getResult())
                def desc = resultXml.get("Desc")[0].value()[0].toString().trim()
                if (BankPacket.RETURN_CODE.containsKey(desc)) {
                    status = (BankPacket.RETURN_CODE[desc].value == 0)
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

    /**
     * 单笔汇款 4004
     * @param arg
     */
    def TransferSing(Pack4004 arg) {
        def stringBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        stringBuilder.append("<Result>")
        arg.properties.each {
            def key = it.key as String
            if (key.startsWith('_')) {
                def fkey = key.replace('_', '')
                def value=it.value ==null ? "" : it.value
                stringBuilder.append("<" + fkey + ">" + value+ "</" + fkey + ">")
            }
        }
        stringBuilder.append("</Result>")
        def msg = new MessageBody(stringBuilder.toString())
        def result = [:]
        BankPacket.CreateRequest("4004", msg, null).SendPacket { BankPacket pack ->
            def resultXml = new XmlParser().parseText(pack.messageBody.getResult())
            resultXml.children().each {
                result.put(it.name(), it.value()[0])
            }
            pack
        }
        return result
    }

    //<?xml version="1.0" encoding="UTF-8" ?><Result><OrigThirdVoucher></OrigThirdVoucher><FrontLogNo></FrontLogNo><CcyCode></CcyCode><OutAcctBankName></OutAcctBankName><OutAcctNo></OutAcctNo><InAcctBankName></InAcctBankName><InAcctNo></InAcctNo><InAcctName></InAcctName><TranAmount></TranAmount><UnionFlag></UnionFlag><Yhcljg></Yhcljg><Fee></Fee></Result>
    /**
     * 单笔汇款查询 4005
     * @param OrigThirdVoucher 4004接口上送的ThirdVoucher或者4014上送的SThirdVoucher
     * @param OrigFrontLogNo 银行返回的转账流水号
     * @param OrigThirdLogNo 请求流水号
     * @return
     */
    def TransferSingleQuery(String OrigThirdVoucher, String OrigFrontLogNo, String OrigThirdLogNo = "") {
        def stringBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        stringBuilder.append("<Result>")
        stringBuilder.append("<OrigThirdLogNo>" + UtilityString.RequestFormat(OrigThirdLogNo, 20) + "</OrigThirdLogNo>")
        stringBuilder.append("<OrigThirdVoucher>" + UtilityString.RequestFormat(OrigThirdVoucher, 20) + "</OrigThirdVoucher>")
        stringBuilder.append("<OrigFrontLogNo>" + UtilityString.RequestFormat(OrigFrontLogNo, 14) + "</OrigFrontLogNo>")
        stringBuilder.append("</Result>")
        def msg = new MessageBody(stringBuilder.toString())
        def result = [:]
        BankPacket.CreateRequest("4005", msg, null).SendPacket { BankPacket pack ->
            def resultXml = new XmlParser().parseText(pack.messageBody.getResult())
            resultXml.children().each {
                result.put(it.name(), it.value()[0])
            }
            pack
        }
        return result
    }
}
