package com.jsy.archives

import com.jsy.system.ToDoTask
import org.grails.jaxrs.provider.DomainObjectNotFoundException

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

    def toPay(CommissionInfo com)throws Exception{
        CommissionInfo commissionInfo=CommissionInfo.get(com.comId)
        commissionInfo.sfyfse=com.sfyfse
        commissionInfo.fpje=com.fpje
        commissionInfo.sj=com.sj
        commissionInfo.fkje=com.fkje
        commissionInfo.sqsh=com.sqsh
        commissionInfo.sl=com.sl
        InvestmentArchives investmentArchives=InvestmentArchives.get(commissionInfo.archivesId)
        String type="yw"
        if(commissionInfo.lx==1){type="gl"}
        new Payment(infoId:commissionInfo.id ,zfsj:commissionInfo.zfsj,fpe: commissionInfo.fpje, fundName:commissionInfo.fundName,contractNum:investmentArchives.contractNum,customerName:commissionInfo.skr,khh: commissionInfo.khh,zh: commissionInfo.yhzh, bmjl: commissionInfo.tcr,yfk: commissionInfo.fkje,dflx: type).save(failOnError: true)
        commissionInfo.type=1
        //修改待办任务
        ToDoTask toDoTask=ToDoTask.get(commissionInfo.todoId)
        toDoTask.clr=springSecurityService.getCurrentUser()
        toDoTask.clsj=new Date()
        toDoTask.status=1
        return commissionInfo.save(failOnError: true)
    }

    def initData={

        (1..3).each {
            CommissionInfo commissionInfo=new CommissionInfo(fundName:'fund'+it,customer:'oswaldl',
                    tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:1,ywjl:'admin',
                    tcr:'ss',skr:'bb',khh:'1221121',yhzh:'sss',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:1
            );
            commissionInfo.save(failOnError: true)
        }
        (4..8).each {
            CommissionInfo commissionInfo=new CommissionInfo(fundName:'fund'+it,customer:'oswaldl',
                    tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:1,ywjl:'admin',
                    tcr:'ss',skr:'bb',khh:'1221121',yhzh:'sss',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:0
            );
            commissionInfo.save(failOnError: true)
        }

        CommissionInfo commissionInfo1=new CommissionInfo(fundName:'myfund',customer:'zhangjia',
                tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:1,ywjl:'likequn',
                tcr:'ss',skr:'bb',khh:'1111111',yhzh:'aaaaaaa',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:1
        );
        commissionInfo1.save(failOnError: true)

        CommissionInfo commissionInfo2=new CommissionInfo(fundName:'changjiagnyihao',customer:'liuzhen',
                tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:1,ywjl:'saterday',
                tcr:'ss',skr:'bb',khh:'222222',yhzh:'bbbbbbb',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:1
        );
        commissionInfo2.save(failOnError: true)

        CommissionInfo commissionInfo3=new CommissionInfo(fundName:'tiaozhan',customer:'zhanshen',
                tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:1,ywjl:'lusifa',
                tcr:'ss',skr:'bb',khh:'3333333',yhzh:'cccccc',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:1
        );
        commissionInfo3.save(failOnError: true)

        CommissionInfo commissionInfo4=new CommissionInfo(fundName:'laiba',customer:'songzai',
                tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:1,ywjl:'cheme',
                tcr:'ss',skr:'bb',khh:'444444',yhzh:'ddddddd',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:1
        );
        commissionInfo4.save(failOnError: true)

    }


}
