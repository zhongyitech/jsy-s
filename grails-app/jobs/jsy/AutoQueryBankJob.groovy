package jsy

import com.jsy.bankServices.BankProxyService
import com.jsy.project.ProjectResourceService

/**
 * 银行业务相关的自动运行任务
 * 1,调用银行的转账查询接口
 *    从Payment(兑付表中)获取已经支付,有我银行流水和自定义凭证号的记录,然后调用银行的接口(4005)查询是否已经到账,交设置相关记录的状态
 *    a.兑付表状态更新为已付
 *    b.提成(管理提成/业务提成)的发放时间的设置
 * Created by lioa on 2015/5/14.
 */
class AutoQueryBankJob {
    static triggers = {
        cron name: 'bankQuery', cronExpression: "0/10 * * * * ?"
        //TODO:根据实际需要的情况重新设置间隔时间
    }

    def execute() {
        /*  调秀银行的4004交易接口，查询单笔汇款的处理情况；并根据处理情况设置兑付单及生成兑付单的提成数据的状态 */
        try {
            new BankProxyService().TransferQueryTask()
        } catch (Exception ex) {
            println("执行自动任务错误!")
        }
        //todo:定期执行凭证生成代码
        /*   从银行流水表中的获取数据，按照规则生成凭证，设置银行流水的处理情况  */

    }
}
