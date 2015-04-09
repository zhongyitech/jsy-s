package com.jsy.system

import grails.plugin.asyncmail.AsynchronousMailService
import grails.transaction.Transactional

import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Transactional(rollbackFor = Throwable.class)
class SendMailService {

    AsynchronousMailService asynchronousMailService

    def serviceMethod() {

    }

    def toMail(MailBean mailBean) {
        try{
            asynchronousMailService.sendAsynchronousMail {
                from "my061830@163.com"
                to mailBean.mailTo;
                subject mailBean.subject
                html mailBean.content
                //attachBytes 'test.txt', 'text/plain', AttachFile ContentBytes
                // 附加的异步参数
                // 在一分钟后启动，缺省是当前时间启动
                //beginDate new Date(System.currentTimeMillis() + 60000)
                // 必须在一小时内发送，缺省是无穷大
                endDate new Date(System.currentTimeMillis() + 3600000)
                //尝试发送的最大次数为3，缺省值为1
                maxAttemptsCount 3
                //尝试发送的时间间隔，缺省为5分钟
                attemptInterval 30000
            }
        }catch(e){print "fail to send mail to "+mailBean.mailTo}
    }

}
