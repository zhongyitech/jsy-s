package jsy

import com.jsy.archives.CommissionInfoResourceService
import com.jsy.archives.PaymentInfoResourceService

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
        cron name: 'tcAndPay', cronExpression: "0/10 * * * * ?"
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

        //TODO:兑付单
        try {
            println("Start Task Build PaymentInfo(兑付单) ...")
            (paymentInfoResourceService ?: new PaymentInfoResourceService()).addPaymentInfo()
            println("Task Build  PaymentInfo(兑付单)  Completed!")
        } catch (Exception ex) {
            println("Build PaymentInfo(兑付单) Error:" + ex.message)
        }
    }
}
