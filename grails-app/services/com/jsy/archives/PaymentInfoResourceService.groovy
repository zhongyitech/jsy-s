package com.jsy.archives

import com.jsy.system.ToDoTask
import grails.transaction.Transactional
import groovy.sql.Sql
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

import javax.sql.DataSource

@Transactional(rollbackFor = Throwable.class)
class PaymentInfoResourceService {
    def springSecurityService
//    DataSource dataSource

    def create(PaymentInfo dto) {
        dto.save()
    }

    def read(id) {
        def obj = PaymentInfo.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(PaymentInfo.class, id)
        }
        obj
    }

    def readAll() {
        PaymentInfo.findAllByIsAllow(false)
    }

    def update(PaymentInfo dto) {
        def obj = PaymentInfo.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(PaymentInfo.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = PaymentInfo.get(id)
        if (obj) {
            obj.delete()
        }
    }

    //新的生成兑付申请
    /**
     * 循环投资档案,生成兑付查询数据(待兑付的记录)
     * 条件:1.档案已经入库
     * @return
     */
    def addPaymentInfo() {
        //获取没有生成过兑付记录的投资档案
        List<InvestmentArchives> investmentArchives = InvestmentArchives.findAllByStopPay(false)
        println("")
        investmentArchives.each {
            try {
                int t = PaymentInfo.findAllByArchivesId(it.id).size()
                //todo: status==1 正常(已入库)
                if (it.customer != null && it.status == INVESTMENT_STATUS.Normal.value && it.payTimes.size() > t) {
                    println("ContractNum: $it.contractNum Check...")
                    it.payTimes.each { p ->
                        if (p.px == t + 1 && p.sffx == false) {
                            if (p.fxsj.before(new Date())) {
                                //根据档案的客户分配，新增一条记录
                                PaymentInfo paymentInfo = new PaymentInfo()
                                paymentInfo.archivesId = it.id
                                paymentInfo.fxsj = p.fxsj
                                paymentInfo.fundName = it.fund.fundName
                                paymentInfo.htbh = it.contractNum
                                paymentInfo.customerName = it.customer.name
                                paymentInfo.tzje = it.tzje
                                paymentInfo.tzqx = it.tzqx
                                paymentInfo.syl = it.nhsyl
                                paymentInfo.bmjl = it.bmjl?.chainName
                                paymentInfo.khh = it.customer.khh
                                paymentInfo.yhzh = it.customer.yhzh
                                paymentInfo.gj = it.customer.country
                                paymentInfo.zjlx = it.customer.credentialsType
                                paymentInfo.zjhm = it.customer.credentialsNumber
                                //计算应付利息
                                BigDecimal yflx = (it.tzje * it.nhsyl) / it.payTimes.size()
                                paymentInfo.yflx = yflx
                                //计算应付本金
                                BigDecimal yfbj = 0
                                if (t == p.px + 1 && it.fxfs != "D") {
                                    yfbj = it.bj
                                }
                                paymentInfo.yfbj = yfbj
                                paymentInfo.zj = yflx + yfbj
                                paymentInfo.save(failOnError: true)
                                //将这次付息改为已付
                                p.sffx = true
                                p.save(failOnError: true)
                                if (t == p.px + 1) {
                                    it.bj = 0
                                    //将档案表的付款状态改为付款完成
                                    it.stopPay = true
                                    it.save(failOnError: true)
                                }
                                print(it.fundName + " : save ok!")
                            }
                        }
                    }
                } else {
                }
            } catch (Exception ex) {
                print(ex)
            }
        }
    }

    /**
     * 生成兑付申请单
     * @param payId
     * @return
     * @throws Exception
     */
    def toPay(Long payId) throws Exception {
        PaymentInfo paymentInfo = PaymentInfo.get(payId)
        if (paymentInfo.type != 0) {
            return new Exception('正在处理或已处理！')
        }
        //提交到OA不能修改了
        paymentInfo.type = 1
        //OA通过了
        if (paymentInfo.yflx != 0) {
            new Payment(infoId: paymentInfo.id, zfsj: paymentInfo.zfsj, fpe: paymentInfo.yflx, fundName: paymentInfo.fundName, contractNum: paymentInfo.htbh, customerName: paymentInfo.customerName, khh: paymentInfo.khh, zh: paymentInfo.yhzh, bmjl: paymentInfo.bmjl, yfk: paymentInfo.yflx, dflx: "lx").save(failOnError: true)
        }
        if (paymentInfo.yfbj != 0) {
            new Payment(infoId: paymentInfo.id, zfsj: paymentInfo.zfsj, fpe: paymentInfo.yfbj, fundName: paymentInfo.fundName, contractNum: paymentInfo.htbh, customerName: paymentInfo.customerName, khh: paymentInfo.khh, zh: paymentInfo.yhzh, bmjl: paymentInfo.bmjl, yfk: paymentInfo.yfbj, dflx: "bj").save(failOnError: true)
        }
        paymentInfo.type = 2
        paymentInfo.isAllow = true
        paymentInfo.save(failOnError: true)
        //修改待办任务
        ToDoTask toDoTask = ToDoTask.get(paymentInfo.todoId)
        toDoTask.clr = springSecurityService.getCurrentUser()
        toDoTask.clsj = new Date()
        toDoTask.status = 1
    }

    /**
     * 获取已付利息和已付本金的操作
     * @param investment
     * @return
     */
    def getPaymentAmount(Long investment) {

//        def sql=new Sql(dataSource)

        def bj = 0
        def lx = 0
        PaymentInfo.findAllByArchivesIdAndIsAllowAndType(investment, true, 2)
                .each {
            bj += it.getYfbj()
            lx += it.getYflx()
        }
        return [bj: bj, lx: lx]
    }
    /**
     * 完结兑付记录
     * 1.设置状态为已支付
     * 2.更新数据修改时间
     * 3.调用待办事项的结束方法
     */
    def setSuccess(PaymentInfo payInfo) {
        payInfo.isAllow = true
        payInfo.lastUpdated = new Date()
        //设备待办事项为完成状态
        def todoTask = ToDoTask.get(payInfo.todoId)
        if (todoTask != null) {
            todoTask.status = 1
            todoTask.clsj = new Date()
            todoTask.save(failOnError: true)
        }
    }
}
