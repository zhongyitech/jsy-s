package jsy

import com.jsy.archives.CommissionInfoResourceService
import com.jsy.archives.PaymentInfoResourceService
import com.jsy.archives.UserCommision
import com.jsy.system.NotificationMail
import com.jsy.utility.DateUtility
import grails.plugin.asyncmail.AsynchronousMailService

/**
 * 任务:
 * 1.自动生成管理提成
 * 2.自动生成兑付 (投资档案为已入档案的才生成)
 * Created by lioa on 2015/5/14.
 */
class AutoBuildGltcJob {
    PaymentInfoResourceService paymentInfoResourceService
    CommissionInfoResourceService commissionInfoResourceService

    static triggers = {
        //todo:set everyDay
        cron name: 'tcAndPay', cronExpression: "0/30 0 * * * ?"
//        cron name: 'tcAndPay', cronExpression: "0/30 0 0 * * ?"
    }

    def execute() {
        //TODO:管理提成单
        try {
            println("Start Task Build  CommissionInfo(管理提成)...")
            (commissionInfoResourceService ?: new CommissionInfoResourceService()).addCommissionInfo()
            println("Task Build  CommissionInfo(管理提成) Completed!")
        } catch (Exception ex) {
            println("Build  CommissionInfo(管理提成) Error:" + ex.message)
        }

        //TODO:兑付单(利息和本金兑付单)
        try {
            println("Start Task Build PaymentInfo(兑付单) ...")
            (paymentInfoResourceService ?: new PaymentInfoResourceService()).addPaymentInfo()
            println("Task Build  PaymentInfo(兑付单)  Completed!")
        } catch (Exception ex) {
            println("Build PaymentInfo(兑付单) Error:" + ex.message)
        }

        Calendar rightNow = Calendar.getInstance();
        def nowDt = DateUtility.lastDayWholePointDate(new Date())
        rightNow.setTime(nowDt);

        //TODO:查询提成单,根据规则发邮件给业务经理
        try {
            Date sendDate = DateUtility.lastDayWholePointDate(new Date(new Date().getTime() - (10 * 24 * 60 * 60 * 1000)));
            UserCommision.findAllByTcffsjOrGlffsj2OrGlffsj3(sendDate, sendDate, sendDate)
                    .each {
                //只要有一个时间滑支付的都要处理
                if (it.real_glffsj2 != null && it.real_glffsj3 != null && it.sjffsj != null) return
                Date sDate = null
                if (it.glffsj2 != null) {
                    //管理提成
                    if (it.real_glffsj3 == null)
                        sDate = it.glffsj3
                    if (it.real_glffsj2 == null)
                        sDate = it.glffsj2
                    if (it.sjffsj == null)
                        sDate == it.tcffsj
                } else {
                    //业务提成
                    if (it.sjffsj == null)
                        sDate = it.tcffsj
                }
                if (sDate == null) return
                String strD = sDate.toString()
                def user = it.user
                try {
                    def femail = NotificationMail.findByUserCommisionIdAndPayTimeAnd(it.id, sDate)
                    if (femail == null) {
                        println("Start Send Email:$user.email $user.chainName")
                        String htmls = '<body><u>' + "$user.chainName 你好! 您的有一笔管理提成将在 $strD 发放,请注意. " + '</u></body>'
                        new AsynchronousMailService().sendMail {
                            from 'oswaldl2009@126.com';
                            to user.email;
                            subject '管理提成到期提醒';
                            html htmls;
                            // Additional asynchronous parameters (optional)
                            maxAttemptsCount 3;   // Max 3 attempts to send, default 1
                            attemptInterval 300000;    // Minimum five minutes between attempts, default 300000 ms
                            delete true;    // Marks the message for deleting after sent
                            immediate true;    // Run the send job after the message was created
                            priority 10;   // If priority is greater then message will be sent faster
                        }
                        new NotificationMail(userCommisionId: it.id, payTime: sDate, emailTxt: htmls)
                                .save(failOnError: true)
                        println("Send Email OK:$user.email")
                    }
                } catch (Exception ex) {
                    println("Send Email ERROR: $ex.message")
                }
            }
        } catch (Exception ex) {
            println(ex.message)
        }
    }
}