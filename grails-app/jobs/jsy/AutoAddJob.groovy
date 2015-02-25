package jsy



class AutoAddJob {
    def paymentInfoResourceService
    static triggers = {
        cron name: 'myTrigger', cronExpression: "0 0 4 * * ?"
//      simple repeatInterval: 5000l // execute job once in 5 seconds
    }

    def execute() {
        paymentInfoResourceService.addPaymentInfo()
    }
}
