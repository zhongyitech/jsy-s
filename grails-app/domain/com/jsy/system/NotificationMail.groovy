package com.jsy.system

/**
 * 提成邮件通知记录
 */
class NotificationMail {
    Long userCommisionId
    Date payTime
    boolean isSend = false
    String emailTxt
    static constraints = {
    }
}
