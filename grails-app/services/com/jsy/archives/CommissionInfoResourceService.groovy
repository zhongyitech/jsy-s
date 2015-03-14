package com.jsy.archives

import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund
import com.jsy.system.ToDoTask
import com.jsy.system.TypeConfig
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
        return commissionInfo.save(failOnError: true)
    }

    def initData={
        Customer customer = new Customer(name:'liayi',credentialsAddr:'at sss',credentialsNumber:'1111');
        customer.save(failOnError: true)

        CustomerArchives customerArchive = new CustomerArchives(
                name:'1',
                country:'2',
                credentialsType:'3',
                credentialsNumber:'4',
                credentialsAddr:'5',
                fddbr:'6',
                zch:'7',
                khh:'8',
                telephone:'9',
                phone:'10',
                postalcode:'11',
                callAddress:'12');
        customerArchive.save(failOnError: true)

        def typeConfig =TypeConfig.findByMapName("在库")
        def fund = Fund.findByFundName('fund1')?:new Fund(fundName:'fund1',fundNo:'F001',startSaleDate:new Date(),status:TypeConfig.findByTypeAndMapValue(1,2),owner:'张三',memo:'备注').save(failOnError: true)
        def user = User.findByUsername("admin")


        InvestmentArchives archives1 = new InvestmentArchives(markNum:'2141412',customerArchive:customerArchive,customer:customer,
                archiveNum:1,contractNum:'124214',fund:fund,tzje:1,tzqx:'2015-06-01',rgrq:new Date(),dqrq:new Date(),fxfs:'fukuan',nhsyl:0.01,
                htzt:typeConfig,bm:'abc',gltc:0.1,ywtc:0.1,bj:10000,
        )
        archives1.save(failOnError: true)

        InvestmentArchives archives2 = new InvestmentArchives(markNum:'124122',customerArchive:customerArchive,customer:customer,
                archiveNum:2,contractNum:'34235',fund:fund,tzje:1,tzqx:'2015-06-01',rgrq:new Date(),dqrq:new Date(),fxfs:'fukuan',nhsyl:0.01,
                htzt:typeConfig,bm:'abc',gltc:0.1,ywtc:0.1,bj:10000,
        )
        archives2.save(failOnError: true)

        //开始初始化N条未提成申请状态的提成申请,lx=1为管理
        (1..9).each {
            CommissionInfo commissionInfo=new CommissionInfo(fundName:'fund'+it,customer:'oswaldl',
                    tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:archives1.id,ywjl:'admin',
                    tcr:'ss',skr:'bb',khh:'1221121',yhzh:'sss',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:1,type:0
            );
            commissionInfo.save(failOnError: true)
        }
        //开始初始化N条提成申请状态的提成申请，lx=1为管理
        (10..19).each {
            CommissionInfo commissionInfo=new CommissionInfo(fundName:'fund'+it,customer:'oswaldl',
                    tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:archives1.id,ywjl:'admin',
                    tcr:'ss',skr:'bb',khh:'1221121',yhzh:'sss',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:1,type:1
            );
            commissionInfo.save(failOnError: true)
        }

        //开始初始化N条未提成申请状态的提成申请，lx=0为业务
        (20..29).each {
            CommissionInfo commissionInfo=new CommissionInfo(fundName:'fund'+it,customer:'oswaldl',
                    tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:archives2.id,ywjl:'admin',
                    tcr:'ss',skr:'bb',khh:'1221121',yhzh:'sss',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:0,type:0
            );
            commissionInfo.save(failOnError: true)
        }
        //开始初始化N条提成申请状态的提成申请，lx=0为业务
        (30..39).each {
            CommissionInfo commissionInfo=new CommissionInfo(fundName:'fund'+it,customer:'oswaldl',
                    tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:archives2.id,ywjl:'admin',
                    tcr:'ss',skr:'bb',khh:'1221121',yhzh:'sss',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:0,type:1
            );
            commissionInfo.save(failOnError: true)
        }

        CommissionInfo commissionInfo1=new CommissionInfo(fundName:'myfund',customer:'zhangjia',
                tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:archives1.id,ywjl:'likequn',
                tcr:'ss',skr:'bb',khh:'1111111',yhzh:'aaaaaaa',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:1
        );
        commissionInfo1.save(failOnError: true)

        CommissionInfo commissionInfo2=new CommissionInfo(fundName:'changjiagnyihao',customer:'liuzhen',
                tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:archives1.id,ywjl:'saterday',
                tcr:'ss',skr:'bb',khh:'222222',yhzh:'bbbbbbb',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:1
        );
        commissionInfo2.save(failOnError: true)

        CommissionInfo commissionInfo3=new CommissionInfo(fundName:'tiaozhan',customer:'zhanshen',
                tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:archives1.id,ywjl:'lusifa',
                tcr:'ss',skr:'bb',khh:'3333333',yhzh:'cccccc',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:1
        );
        commissionInfo3.save(failOnError: true)

        CommissionInfo commissionInfo4=new CommissionInfo(fundName:'laiba',customer:'songzai',
                tzje:1,syl:0.1,tcje:0.1,tcl:0.1,rgqx:'2015-08-15',rgrq:new Date(),archivesId:archives1.id,ywjl:'cheme',
                tcr:'ss',skr:'bb',khh:'444444',yhzh:'ddddddd',sfgs:false,sl:0.1,sqsh:false,sfyfse:false,lx:1
        );
        commissionInfo4.save(failOnError: true)



        (1..9).each {
            PaymentInfo paymentInfo = new PaymentInfo(fundName:'fund'+it,htbh:'1000',customerName:'sss'+it,tzje:12,
                    tzqx:'2015-05-14',syl:0.1,yflx:0.1,yfbj:100,
                    zj:100,khh:'211111',yhzh:'aaaaa',gj:'bbb',zjlx:'dd',zjhm:'ddd',bmjl:'ddd',
                    archivesId:archives1.id,fxsj:new Date(),type:0)
            paymentInfo.save(failOnError: true)
        }

        (10..19).each {
            PaymentInfo paymentInfo = new PaymentInfo(fundName:'fund'+it,htbh:'1000',customerName:'sss'+it,tzje:12,
                    tzqx:'2015-05-14',syl:0.1,yflx:0.1,yfbj:100,
                    zj:100,khh:'211111',yhzh:'aaaaa',gj:'bbb',zjlx:'dd',zjhm:'ddd',bmjl:'ddd',
                    archivesId:archives1.id,fxsj:new Date(),type:1)
            paymentInfo.save(failOnError: true)
        }



    }


}
