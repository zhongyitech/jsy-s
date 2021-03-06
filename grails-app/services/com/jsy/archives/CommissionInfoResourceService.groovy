package com.jsy.archives

import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund
import com.jsy.system.ToDoTask
import com.jsy.system.TypeConfig
import com.jsy.utility.MyException
import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class CommissionInfoResourceService {
    def springSecurityService

    def create(CommissionInfo dto) {
        dto.save()
    }

    def read(id) {
        def obj = CommissionInfo.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(CommissionInfo.class, id)
        }
        obj
    }

    def readAll() {
        CommissionInfo.findAll()
    }

    def update(CommissionInfo dto) {
        def obj = CommissionInfo.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(CommissionInfo.class, dto.id)
        }
        obj.union(dto)
        obj
    }

    void delete(id) {
        def obj = CommissionInfo.get(id)
        if (obj) {
            obj.delete()
        }
    }

    //自动生成管理提成查询
    /**
     *  管理提成 生成代码:生成提成的代码
     *
     * @return
     */
    //TODO:添加到自动运行任务
    def addCommissionInfo() {
        List<InvestmentArchives> investmentArchives = InvestmentArchives.findAllByDazt(0)
        investmentArchives.each {
            it.gltcs.each { gltc ->
                int s = CommissionInfo.findAllByTcrAndArchivesIdAndLx(gltc.user.chainName, it.id, 1).size()
                if (s == 0) {
                    //添加第一次管理提成,取70%
                    if (gltc.tcffsj && gltc.tcffsj.before(new Date())) {
                        def c = new CommissionInfo(userCommision: gltc, zfsj: gltc.tcffsj,
                                tcje: gltc.tcje * 0.7, lx: 1, fundName: it.fund.fundName, customer: it.customer.name, tzje: it.sjtzje, syl: it.nhsyl, archivesId: it.id, rgqx: it.tzqx, rgrq: it.rgrq, tcr: gltc.user.chainName, ywjl: it.ywjl.chainName, skr: gltc.skr, yhzh: gltc.yhzh, khh: gltc.khh, sfgs: !gltc.user.isUser, tcl: it.ywtc)
                        setRateAndAmount(it.fund, gltc, c)
                        c.save(failOnError: true)
                    }
                } else if (s == 1) {
                    if (gltc.glffsj2 && gltc.glffsj2.before(new Date())) {
                        def
                                c = new CommissionInfo(zfsj: gltc.glffsj2,
                                        tcje: gltc.tcje * 0.2, lx: 1, fundName: it.fund.fundName, customer: it.customer.name, tzje: it.sjtzje, syl: it.nhsyl, archivesId: it.id, rgqx: it.tzqx, rgrq: it.rgrq, tcr: gltc.user.chainName, ywjl: it.ywjl.chainName, skr: gltc.skr, yhzh: gltc.yhzh, khh: gltc.khh, sfgs: !gltc.user.isUser, tcl: it.ywtc, userCommision: gltc)
                        setRateAndAmount(it.fund, gltc, c)
                        c.save(failOnError: true)
                    }
                } else if (s == 2) {
                    if (gltc.glffsj3 && gltc.glffsj3.before(new Date())) {
                        def c = new CommissionInfo(userCommision: gltc, zfsj: gltc.glffsj3,
                                tcje: gltc.tcje * 0.1, lx: 1, fundName: it.fund.fundName, customer: it.customer.name, tzje: it.sjtzje, syl: it.nhsyl, archivesId: it.id, rgqx: it.tzqx, rgrq: it.rgrq, tcr: gltc.user.chainName, ywjl: it.ywjl.chainName, skr: gltc.skr, yhzh: gltc.yhzh, khh: gltc.khh, sfgs: !gltc.user.isUser, tcl: it.ywtc)
                        setRateAndAmount(it.fund, gltc, c)
                        c.save(failOnError: true)

                    }
                }
            }
        }
    }

    def addYWCommissionInfo() {

    }
    /**
     * 计算提成的税额和发票及支付额
     * @param fund 基金
     * @param uc 提成数据
     */
    static def setRateAndAmount(Fund fund, UserCommision uc, CommissionInfo outInfo) {
        def tcfp = fund.tcfpfw.find { tc ->
            uc.user.department.leader && (tc.manageerId == uc.user.department.leader.id)
        }
        if (tcfp == null)
            throw new MyException("提成分配设置不正确，该业务经理的部门没有设置提成分配数据！" + uc.user.chainName)
        outInfo.sfgs = (uc.cardType == 1)
        outInfo.sqsh = tcfp.rateBefore
        outInfo.sl = tcfp.rate
        //个人收款
        if (uc.cardType == 0) {
            outInfo.sj = uc.tcje * tcfp.rate
            //发票金额
            outInfo.fpje = tcfp.rateBefore ? uc.tcje : uc.tcje * (1 + tcfp.rate)
            //支付金额
            outInfo.fkje = tcfp.rateBefore ? uc.tcje * (1 - tcfp.rate) : uc.tcje
            outInfo.sfyfse = false
        } else {
            //公司(机构)收款
            outInfo.sj = tcfp.rateBefore ? 0 : uc.tcje * tcfp.rate
            //发票金额
            outInfo.fpje = tcfp.rateBefore ? uc.tcje : uc.tcje * (1 + tcfp.rate)
            //支付金额
            outInfo.fkje = tcfp.rateBefore ? uc.tcje : uc.tcje * (1 + tcfp.rate)
            outInfo.sfyfse = true
        }
    }

    /**
     * 生成提成申请单(Payment)
     * @param comId
     * @param sfyfse
     * @param fpje 发票金额
     * @param sj 提成税金
     * @param fkje 付款金额
     * @param sqsh
     * @param sl 税率
     * @return
     * @throws Exception
     */
    def toPay(comId, sfyfse, fpje, sj, fkje, sqsh, sl) throws Exception {
        if (sl == 0 || sl >= 1) {
            throw new MyException("税率应该设置为大于0,并且小于1.")
        }
        CommissionInfo commissionInfo = CommissionInfo.get(comId)
        if (commissionInfo.type != 0) {
            println '正在处理或已处理！'
            throw new Exception('正在处理或已处理！')
        }
        commissionInfo.sfyfse = sfyfse
        commissionInfo.fpje = fpje
        commissionInfo.sj = sj
        commissionInfo.fkje = fkje
        commissionInfo.sqsh = sqsh
        commissionInfo.sl = sl
        InvestmentArchives investmentArchives = InvestmentArchives.get(commissionInfo.archivesId)
        String type = "yw"
        if (commissionInfo.lx == 1) {
            type = "gl"
        }
        new Payment(
                glPayCount: type == "gl" ? commissionInfo.zfsj : null,    //如果是管理提成,设置为应支付时间(此时间与管理提成的几次发放时间是对应的)
                infoId: commissionInfo.id,
                zfsj: commissionInfo.zfsj,
                fpe: commissionInfo.fpje,
                fundName: commissionInfo.fundName,
                contractNum: investmentArchives?.contractNum,
                customerName: commissionInfo.skr,
                khh: commissionInfo.khh,
                zh: commissionInfo.yhzh,
                bmjl: commissionInfo.tcr,
                yfk: commissionInfo.fkje,
                dflx: type)
                .save(failOnError: true)
        commissionInfo.type = 1
        //修改待办任务
        ToDoTask toDoTask = ToDoTask.get(commissionInfo.todoId)
        toDoTask.clr = springSecurityService.getCurrentUser() as User
        toDoTask.clsj = new Date()
        toDoTask.status = 1
        commissionInfo.save(failOnError: true)
        return true
    }
}
