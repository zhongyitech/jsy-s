package com.jsy.system

/**
 * ����ʼ�֪ͨ��¼
 */
class NotificationMail {
    Long userCommisionId
    Date payTime
    boolean isSend = false
    String emailTxt
    static constraints = {
    }
}
