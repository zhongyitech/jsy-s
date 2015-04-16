package com.jsy.utility

/**
 * ƾ֤���ɵİ�����
 * Created by lishizhong on 2015/4/14.
 */
class BankOrderUntil {

    private Map<String, Closure<String>> list = [:]

    BankOrderUntil() {
        init()
    }
    private static BankOrderUntil _instance = new BankOrderUntil()

    public static BankOrderUntil Instance() {
        return _instance
    }

    public String GetFormatValue(String text, def argobj) {
        print("text:" + text)
        list.each {
            text = text.replace(it.getKey(), it.getValue().call(argobj))
        }
        print("text replace:" + text)

        return text
    }

    def init() {
        list.put("[开户行]", {
            arg ->
                return arg.bank.bankOfDeposit
        })
        list.put("[公司名称]", {
            arg ->
                return arg.company.companyName
        })
        list.put("[账号后4位]", {
            arg ->
                if (arg.bank.account.length() <= 4)
                    return arg.bank.account
                return arg.bank.account.substring(arg.bank.account.length() - 4)
        })
        list.put("[对方户名]", {
            arg ->
                return arg.record.otherSideName
        })
        list.put("[对方账号]", {
            arg ->
                return arg.record.otherSideAccount
        })
    }
}
