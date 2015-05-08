package com.jsy.archives

import com.jsy.auth.User

/**
 * 业务提成信息
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
    //提成应该发放时间
    Date tcffsj
    //实际发放时间
    Date sjffsj

    //管理提成发放时间
    Date glffsj2
    Date glffsj3



    static constraints = {
        tcffsj nullable: true
        sjffsj nullable: true
        glffsj2 nullable: true
        glffsj3 nullable: true
    }
}
