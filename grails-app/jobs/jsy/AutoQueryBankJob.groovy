package jsy

import com.jsy.bankServices.BankProxyService
import com.jsy.project.ProjectResourceService

/**
 * 银行业务相关的自动运行任务
 * 1,调用银行的转账查询接口
 *    从Payment(兑付表中)获取已经支付,有我银行流水和自定义凭证号的记录,然后调用银行的接口(4005)查询是否已经到账,交设置相关记录的状态
 *    a.兑付表状态更新为已付
 *    b.提成(管理提成/业务提成)的发放时间的设置
 *    c.
 * Created by lioa on 2015/5/14.
 */
class AutoQueryBankJob {
//    BankProxyService bankProxyService
    ProjectResourceService projectResourceService
    static triggers = {
        cron name: 'bankQuery', cronExpression: "0/10 * * * * ?"
    }
    def execute() {
        //查询银行交易信息
        try {
           new BankProxyService().TransferQueryTask()
        } catch (Exception ex) {
            println("执行自动任务错误!")
        }

        //todo:定期执行凭证生成代码

    }
}
