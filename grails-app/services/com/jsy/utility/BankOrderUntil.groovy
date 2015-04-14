package com.jsy.utility

/**
 * 凭证生成的帮助类
 * Created by lishizhong on 2015/4/14.
 */
class BankOrderUntil {

    private Map<String, Closure> list = [:]

    BankOrderUntil() {
        init()
    }
    private static BankOrderUntil _instance = new BankOrderUntil()

    public static BankOrderUntil Instance() {
        return _instance
    }

    public String GetFormatValue(def name, def argobj) {
        if (list.containsKey(name)) {
            return list.get(name).call(argobj)
        }
    }

    def init() {
        list.put("[公司名称]", {
            arg ->
                print(arg)
                return "test"
        })
    }
}
