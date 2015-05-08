package com.jsy.bankServices

import com.jsy.bankPacket.Pack4004
import com.jsy.utility.UtilityString
import grails.converters.XML

/**
 * 银行业务代理服务类
 * Created by lioa on 2015/4/23.
 */
class BankProxyService {
    static final String R4005_SUCCESS = "转账交易成功"

    /**
     * 查询账户余额查询 4001
     * @param arg [Account,CcyCode ]
     * @return
     */
    public def QueryBalance(Map arg) {
        def sb = new StringBuilder()
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
        sb.append("<Result>")
        sb.append("<Account>" + arg.Account + "</Account>")
        sb.append("<CcyCode>" + arg.CcyCode + "</CcyCode>")
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
        sb.append("<AcctNo>" + arg.AcctNo + "</AcctNo>")
        sb.append("<CcyCode>" + arg.CcyCode + "</CcyCode>")
        sb.append("<BeginDate>" + arg.BeginDate + "</BeginDate>")
        sb.append("<EndDate>" + arg.EndDat + "</EndDate>")
        sb.append("<PageNo>" + arg.PageNo + "</PageNo>")
        sb.append("<PageSize>" + arg.PageSize + "</PageSize>")
        sb.append("<Reserve>" + arg.Reserve + "</Reserve>")
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

    /*<?xml version="1.0" encoding="UTF-8" ?><Result><ThirdVoucher></ThirdVoucher><FrontLogNo></FrontLogNo><CcyCode></CcyCode><OutAcctName></OutAcctName><OutAcctNo></OutAcctNo><InAcctBankName></InAcctBankName><InAcctNo></InAcctNo><InAcctName></InAcctName><TranAmount></TranAmount><UnionFlag></UnionFlag><Fee1></Fee1><Fee2></Fee2><SOA_VOUCHER></SOA_VOUCHER><hostFlowNo></hostFlowNo></Result>
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
                def value = it.value == null ? "" : it.value
                stringBuilder.append("<" + fkey + ">" + value + "</" + fkey + ">")
            }
        }
        stringBuilder.append("</Result>")
//        print("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Result><ThirdVoucher></ThirdVoucher><FrontLogNo></FrontLogNo><CcyCode></CcyCode><OutAcctName></OutAcctName><OutAcctNo></OutAcctNo><InAcctBankName></InAcctBankName><InAcctNo></InAcctNo><InAcctName></InAcctName><TranAmount></TranAmount><UnionFlag></UnionFlag><Fee1></Fee1><Fee2></Fee2><SOA_VOUCHER></SOA_VOUCHER><hostFlowNo></hostFlowNo></Result>"
//        .getBytes("GBK").length)
        def msg = new MessageBody(stringBuilder.toString())
        def result = [:]

        BankPacket.CreateRequest("4004", msg, null).SendPacket { BankPacket pack ->
            def resultXml = new XmlParser().parseText(pack.messageBody.getResult())
            resultXml.children().each {
                result.put(it.name(), it.value()[0])
            }
            //成功请求之后保存到数据库
            arg.save(failOnError: true)
            pack
        }
        return result
    }

    //<?xml version="1.0" encoding="UTF-8" ?><Result><OrigThirdVoucher></OrigThirdVoucher><FrontLogNo></FrontLogNo><CcyCode></CcyCode><OutAcctBankName></OutAcctBankName><OutAcctNo></OutAcctNo><InAcctBankName></InAcctBankName><InAcctNo></InAcctNo><InAcctName></InAcctName><TranAmount></TranAmount><UnionFlag></UnionFlag><Yhcljg></Yhcljg><Fee></Fee></Result>
    /**
     * 单笔汇款查询 4005
     * @param OrigThirdVoucher 4004接口上送的ThirdVoucher或者4014上送的SThirdVoucher
     * @param OrigFrontLogNo 银行返回的转账流水号
     * @param OrigThirdLogNo 请求流水号(不建议使用
     * @return
     */
    def TransferSingleQuery(String OrigThirdVoucher, String OrigFrontLogNo, String OrigThirdLogNo = "") {
        def stringBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        stringBuilder.append("<Result>")
        stringBuilder.append("<OrigThirdLogNo>" + OrigThirdLogNo + "</OrigThirdLogNo>")
        stringBuilder.append("<OrigThirdVoucher>" + OrigThirdVoucher + "</OrigThirdVoucher>")
        stringBuilder.append("<OrigFrontLogNo>" + OrigFrontLogNo + "</OrigFrontLogNo>")
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
        def Yhcljg = result["Yhcljg"] as String
        def code = Yhcljg.substring(0, 6)
        def responseData = [resultData: result, success: false, code: code, msg: Yhcljg.substring(6).trim()]
        switch (code) {
            case "000000":
                if (responseData.msg == R4005_SUCCESS) {
                    responseData.success = true
                }
                break
            default:
                responseData.success = false
                break
        }
        return responseData
    }
}
