package com.jsy.archives

import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund
import com.jsy.system.ToDoTask
import com.jsy.system.TypeConfig
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
    def addCommissionInfo(){
        List<InvestmentArchives> investmentArchives=InvestmentArchives.findAllByDazt(0)
        investmentArchives.each {
                it.gltcs.each {gltc->
                int s=CommissionInfo.findAllByTcrAndArchivesIdAndLx(gltc.user.chainName,it.id,1).size()
                if(s==0){
                    //添加第一次管理提成,取70%
                    if(gltc.tcffsj&&gltc.tcffsj.before(new Date())){
                        new CommissionInfo(tcje: gltc.tcje*0.7,lx: 1, fundName:it.fund.fundName,customer: it.customer.name,tzje: it.sjtzje,syl: it.nhsyl,archivesId:it.id,rgqx: it.qx,rgrq: it.rgrq,tcr: gltc.user.chainName,ywjl: it.ywjl,skr: gltc.skr,yhzh: gltc.yhzh,khh: gltc.khh,sfgs: !gltc.user.isUser,tcl:it.ywtc ).save(failOnError: true)
                    }
                }else if(s==1){
                    if(gltc.glffsj2&&gltc.glffsj2.before(new Date())){
                        new CommissionInfo(tcje: gltc.tcje*0.2,lx: 1, fundName:it.fund.fundName,customer: it.customer.name,tzje: it.sjtzje,syl: it.nhsyl,archivesId:it.id,rgqx: it.qx,rgrq: it.rgrq,tcr: gltc.user.chainName,ywjl: it.ywjl,skr: gltc.skr,yhzh: gltc.yhzh,khh: gltc.khh,sfgs: !gltc.user.isUser,tcl:it.ywtc ).save(failOnError: true)
                    }
                }else if(s==2){
                    if(gltc.glffsj3&&gltc.glffsj3.before(new Date())){
                        new CommissionInfo(tcje: gltc.tcje*0.1,lx: 1, fundName:it.fund.fundName,customer: it.customer.name,tzje: it.sjtzje,syl: it.nhsyl,archivesId:it.id,rgqx: it.qx,rgrq: it.rgrq,tcr: gltc.user.chainName,ywjl: it.ywjl,skr: gltc.skr,yhzh: gltc.yhzh,khh: gltc.khh,sfgs: !gltc.user.isUser,tcl:it.ywtc ).save(failOnError: true)
                    }
                }
            }
        }
    }

    def toPay(comId,sfyfse,fpje,sj,fkje,sqsh,sl)throws Exception{
        CommissionInfo commissionInfo=CommissionInfo.get(comId)
        if (commissionInfo.type!=0){
            return new Exception('正在处理或已处理！')
        }
        commissionInfo.sfyfse=sfyfse
        commissionInfo.fpje=fpje
        commissionInfo.sj=sj
        commissionInfo.fkje=fkje
        commissionInfo.sqsh=sqsh
        commissionInfo.sl=sl
        InvestmentArchives investmentArchives=InvestmentArchives.get(commissionInfo.archivesId)
        String type="yw"
        if(commissionInfo.lx==1){type="gl"}
        new Payment(
                infoId:commissionInfo.id ,
                zfsj:commissionInfo.zfsj,
                fpe: commissionInfo.fpje,
                fundName:commissionInfo.fundName,
                contractNum:investmentArchives?.contractNum,
                customerName:commissionInfo.skr,
                khh: commissionInfo.khh,
                zh: commissionInfo.yhzh,
                bmjl: commissionInfo.tcr,
                yfk: commissionInfo.fkje,
                dflx: type).save(failOnError: true)

        commissionInfo.type=1
        //修改待办任务
        ToDoTask toDoTask=ToDoTask.get(commissionInfo.todoId)
        toDoTask.clr=springSecurityService.getCurrentUser()
        toDoTask.clsj=new Date()
        toDoTask.status=1
        commissionInfo.save(failOnError: true)
        return true
    }
}
