package com.jsy.archives

import com.jsy.auth.User
import com.jsy.utility.DateUtility

/**
 * 业务提成信息/管理提成
 */
class UserCommision {

    //用户
    User user
    //收款人
    String skr

    //开户行
    String khh
    //银行账号
    String yhzh
    //提成比例
    double tcbl
    //提成金额
    BigDecimal tcje
    //提成应该发放时间:管理提成:第一次, 业务提成:唯一的一次支付时间
    Date tcffsj
    //实际发放时间
    Date sjffsj

    //管理提成发放时间
    //第二次发放时间
    Date glffsj2
    Date real_glffsj2
    //第三次发放时间
    Date glffsj3
    //实际发放时间
    Date real_glffsj3

    def beforeInsert() {
        if (this.glffsj2 != null) {
            this.glffsj2 = DateUtility.lastDayWholePointDate(this.glffsj2)
        }
        if (this.glffsj3 != null) {
            this.glffsj3 = DateUtility.lastDayWholePointDate(this.glffsj3)
        }
        if (this.tcffsj != null) {
            this.tcffsj = DateUtility.lastDayWholePointDate(this.tcffsj)
        }
    }
    static constraints = {
        tcffsj nullable: true
        sjffsj nullable: true
        glffsj2 nullable: true
        real_glffsj2 nullable: true
        glffsj3 nullable: true
        real_glffsj3 nullable: true
    }
}
