package com.jsy.bankServices

import com.jsy.archives.Payment
import com.jsy.archives.PaymentResourceService
import com.jsy.bankPacket.Pack4004
import com.jsy.utility.MyException
import com.jsy.utility.PAYMENT_STATUS
import com.jsy.utility.UtilityString
import grails.converters.XML
import sun.misc.HexDumpEncoder

/**
 * 银行业务代理服务类
 * Created by lioa on 2015/4/23.
 */
class BankProxyService {
    static final String R4005_SUCCESS = "转账交易成功"
    PaymentResourceService paymentResourceService
    static final String CharSet = "GBK"
    static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"" + CharSet + "\" ?>"

    /**
     * 查询账户余额查询 4001
     * @param arg [Account,CcyCode ]
     * @return
     */
    public def QueryBalance(Map arg) throws Exception {
        try {
            def sb = new StringBuilder()
            sb.append(XML_HEAD)
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
        catch (Exception e) {
            throw new MyException(e.message)
        }
    }

    /**
     * 查询账户历史交易明细 4013
     * @param arg [AcctNo,CcyCode,BeginDate,EndDate,PageNo,PageSize,Reserve]
     * @return
     */
    def TransatcionRecords(Map arg) throws Exception {
        try {
            def sb = new StringBuilder()
            sb.append(XML_HEAD)
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
        catch (Exception e) {
            throw new MyException(e.message)
        }
    }

    /**
     *  系统状态探测 S001
     * @return 服务状态是否可用
     */
    boolean CheckServerStatus() throws Exception {
        try {
            MessageBody msg = new MessageBody(XML_HEAD)
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
        catch (Exception e) {
            throw new MyException(e.message)
        }
    }

    /*"<?xml version=\"1.0\" encoding=\"" + CharSet + "\" ?>"<Result><ThirdVoucher></ThirdVoucher><FrontLogNo></FrontLogNo><CcyCode></CcyCode><OutAcctName></OutAcctName><OutAcctNo></OutAcctNo><InAcctBankName></InAcctBankName><InAcctNo></InAcctNo><InAcctName></InAcctName><TranAmount></TranAmount><UnionFlag></UnionFlag><Fee1></Fee1><Fee2></Fee2><SOA_VOUCHER></SOA_VOUCHER><hostFlowNo></hostFlowNo></Result>
    /**
     * 单笔汇款 4004
     * @param arg
     */

    def TransferSing(Pack4004 arg) throws Exception {
        try {
            def stringBuilder = new StringBuilder(XML_HEAD)
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
//        print(""<?xml version=\"1.0\" encoding=\"" + CharSet + "\" ?>"<Result><ThirdVoucher></ThirdVoucher><FrontLogNo></FrontLogNo><CcyCode></CcyCode><OutAcctName></OutAcctName><OutAcctNo></OutAcctNo><InAcctBankName></InAcctBankName><InAcctNo></InAcctNo><InAcctName></InAcctName><TranAmount></TranAmount><UnionFlag></UnionFlag><Fee1></Fee1><Fee2></Fee2><SOA_VOUCHER></SOA_VOUCHER><hostFlowNo></hostFlowNo></Result>"
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
        catch (Exception ex) {
            ex.printStackTrace()
            throw new MyException(ex.message)
        }
    }

    //"<?xml version=\"1.0\" encoding=\"" + CharSet + "\" ?>"<Result><OrigThirdVoucher></OrigThirdVoucher><FrontLogNo></FrontLogNo><CcyCode></CcyCode><OutAcctBankName></OutAcctBankName><OutAcctNo></OutAcctNo><InAcctBankName></InAcctBankName><InAcctNo></InAcctNo><InAcctName></InAcctName><TranAmount></TranAmount><UnionFlag></UnionFlag><Yhcljg></Yhcljg><Fee></Fee></Result>
    /**
     * 单笔汇款查询 4005
     * @param OrigThirdVoucher 4004接口上送的ThirdVoucher或者4014上送的SThirdVoucher
     * @param OrigFrontLogNo 银行返回的转账流水号
     * @param OrigThirdLogNo 请求流水号(不建议使用
     * @return
     */
    def TransferSingleQuery4005(String OrigThirdVoucher, String OrigFrontLogNo, String OrigThirdLogNo = "") throws Exception {
        try {
            def stringBuilder = new StringBuilder(XML_HEAD)
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
            print(Yhcljg)
            if (Yhcljg == null || Yhcljg.isEmpty() || Yhcljg.length() < 6) {
                throw new Exception("银行处理结果返回码规则不正确!:不能为空或长度小于6位")
            }
            def code = Yhcljg.substring(0, 6)
            def responseData = [resultData: result, success: false, code: code, msg: Yhcljg.substring(7).trim()]
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
        } catch (Exception ex) {
            // ex.printStackTrace()
            throw new MyException(ex.message)
        }
    }

    /**
     * 定时任务:查询未支付的兑付单,从记录中获取银行交易流水->调用4005查询接口确认付款的结果
     * 成功:设置总值单为"已支付"
     *        同步设置的数据:1.提成申请单 2. 兑付申请单 3.投资档案的提成(业务提成/管理提成)记录上的"支付时间"
     * 不成功:留给下一次查询再做处理
     */
    //TODO:添加到自动运行任务中
    void TransferQueryTask() throws Exception {
        try {
            println("执行自动动任务:查询转账确认情况，并设置相关付款数据")
            def payOrders = Payment.findAllByStatus(PAYMENT_STATUS.Paying)
            payOrders.each { Payment pay ->
                try {
                    println('------Bank Code 4004 ------')
                    println("\n查询的4004指令:" + pay.frontLogNo)
                    def result = TransferSingleQuery4005(pay.cstInnerFlowNo, pay.frontLogNo)
                    pay.payStatus = result.code + ":" + result.msg
                    //此总会记录的付款操作成功(银行已返回成功交易的标志),需要更新与此支付想在关的1.兑付单的状态 1.生成此次兑付操作的业务/管理/兑付 的单的状态
                    if (result.success) {
                        (paymentResourceService ?: new PaymentResourceService()).setPaySuccess(pay)
                    }
                } catch (Exception ex) {
                    //不阻赛后续的查询,下次去再尝试运行
                    println("查询支付结果时出错 了!" + ex.message)
                    pay.payStatus = "查询结果出错!"
                }
            }
        } catch (Exception ex) {
            throw new MyException(ex.message)
        }
    }
}
