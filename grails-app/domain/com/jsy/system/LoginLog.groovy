package com.jsy.system
/**
 * 登录日志
 */
class LoginLog {

    //登录账号
    String userName
    //登录密码
    String password
    //验证码
    String verifyCode
    //登录ip
    String loginIp
    //登录时间
    Date loginTime
    //结果*
    String result
    //生成的token
    String token

    static constraints = {
    }
}
