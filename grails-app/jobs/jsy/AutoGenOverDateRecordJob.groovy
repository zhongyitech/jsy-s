package jsy

import com.jsy.project.ReceiveRecordResourceService

/**
 * 每天晚上12点后，自动检查逾期记录的情况，生成ShouldReceiveRecord.
 *
 * 当然，你也可以通过resource接口检查当前是否存在这样的情况
 */
class AutoGenOverDateRecordJob {
    ReceiveRecordResourceService receiveRecordResourceService
    static triggers = {
        cron name: 'overDateTrigger', cronExpression: "10 0 12 * * ?"
    }

    def execute() {
        receiveRecordResourceService.autoGenOverDateRecord()
    }
}
