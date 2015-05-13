package com.jsy.project

import com.jsy.archives.CommissionInfo
import com.jsy.archives.CommissionInfoResourceService
import com.jsy.archives.CustomerArchives
import com.jsy.archives.InvestmentArchives
import com.jsy.archives.PaymentInfo
import com.jsy.auth.Role
import com.jsy.auth.User
import com.jsy.auth.UserRole
import com.jsy.bankConfig.BankAccount
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund
import com.jsy.fundObject.FundCompanyInformation
import com.jsy.fundObject.Kxzqx
import com.jsy.fundObject.Tcfpfw
import com.jsy.fundObject.YieldRange
import com.jsy.system.Company
import com.jsy.system.TypeConfig
import grails.converters.JSON
import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

import java.text.DateFormat
import java.text.SimpleDateFormat

@Transactional(rollbackFor = Throwable.class)
class WorkflowResourceService {
    private static String TAG = "WorkflowResourceService ";

    def initData = {
        initCompany();
        initFund();
        initFund_Company_Relate();
        initInvestment();
//        initCommissionData();
        createProjects()
//        initPayRecords();
        initRole();
        init_flow();
    }

    def init_flow = {
        TSProject.findAll().each { proj ->
            createFlow(proj.id)
        }
    }

    def initRole = {
        Role role1 = Role.findByAuthority("FinancialIncharger") ?: new Role(authority: "FinancialIncharger", name: "财务部负责人").save(failOnError: true);
        Role role2 = Role.findByAuthority("ProjectIncharger") ?: new Role(authority: "ProjectIncharger", name: "项目部负责人").save(failOnError: true);
        Role role3 = Role.findByAuthority("MinistryIncharger") ?: new Role(authority: "MinistryIncharger", name: "法务部负责人").save(failOnError: true);
    }



    def initFund() {
        Fund fund = new Fund(
                fundName: 'fund1',
                fundNo: '001',
                startSaleDate: new Date(),
                raiseFunds:1000000,
                rRaiseFunds:1000000/2,
                quarterRaise:1000000/2/4,
                rQuarterRaise:1000000/2/4/2,
                halfRaise:1000000/2/2,
                rHalfRaise:1000000/2/2/2,
                yearRaise:1000000/2,
                rYearRaise:1000000/2/2,
                status: TypeConfig.findByTypeAndMapValue(1, 2),
                owner: '张三',
                memo: '备注',
        ).save(failOnError: true)

        def admin = User.findByUsername("admin")
        YieldRange range =new YieldRange(investment1:100,investment2:500,yield:0.1,vers:"A").save(failOnError: true);
        Tcfpfw tcfpfw = new Tcfpfw(manageerId:admin.id,businessCommision:0.1,manageCommision:0.2).save(failOnError: true);
        Kxzqx kxzqx = new Kxzqx(jsz:1,dw:'年').save(failOnError: true);

        fund.addToKxzqx(kxzqx)
        fund.addToTcfpfw(tcfpfw)
        fund.addToYieldRange(range)
        fund.save(failOnError: true)


        //2

        Fund fund2 = new Fund(
                fundName: 'fund2',
                fundNo: '002',
                startSaleDate: new Date(),
                raiseFunds:1000000,
                rRaiseFunds:1000000/2,
                quarterRaise:1000000/2/4,
                rQuarterRaise:1000000/2/4/2,
                halfRaise:1000000/2/2,
                rHalfRaise:1000000/2/2/2,
                yearRaise:1000000/2,
                rYearRaise:1000000/2/2,
                status: TypeConfig.findByTypeAndMapValue(1, 2),
                owner: '张三',
                memo: '备注',
        ).save(failOnError: true)

        YieldRange range2 =new YieldRange(investment1:100,investment2:500,yield:0.1,vers:"A").save(failOnError: true);
        Tcfpfw tcfpfw2 = new Tcfpfw(manageerId:admin.id,businessCommision:0.1,manageCommision:0.2).save(failOnError: true);
        Kxzqx kxzqx2 = new Kxzqx(jsz:1,dw:'年').save(failOnError: true);
        Kxzqx kxzqx1 = new Kxzqx(jsz:2,dw:'年').save(failOnError: true);
        fund2.addToKxzqx(kxzqx2)
        fund2.addToKxzqx(kxzqx1)
        fund2.addToTcfpfw(tcfpfw2)
        fund2.addToYieldRange(range2)
        fund2.save(failOnError: true)

    }

    def initInvestment={

    }


    def initCommissionData={
        Customer customer = new Customer(name:'liayi',credentialsAddr:'at sss',credentialsNumber:'1111',credentialsType:'typ1',khh:'平安 ',yhzh:'2352223');
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
        def fund = Fund.findByFundName('fund1')
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

    def createProjects() {
        FundCompanyInformation fundCompanyInformation = FundCompanyInformation.findByCompanyName("金赛银")
        def fund1 = Fund.findByFundName("fund1")
        def fund2 = Fund.findByFundName("fund2")
        def admin = User.findByUsername('admin') ?: new User(
                username: 'admin',
                password: 'admin',
                chainName: "张三",
                enabled: true).save(failOnError: true)

        def i = 1
        new TSProject(
                name: 'project' + i,
                projectDealer: 'dealer_' + i,
                projectOwner: admin,
                creator: admin,
                manage_per: 0.02,
                community_per: 0.03,
                penalty_per: 0.04,
                borrow_per: 0.05,
                interest_per: 0.06,
                year1: 1,
                year2: 0,
                fund: fund1,
                company: fundCompanyInformation,
                interestType: "costCount"
        ).save(failOnError: true)

    }

    @Transactional
    def createFlow(def projectid) {
        def project = TSProject.get(projectid)
        if (!project)
            return;
        def admin = User.findByUsername('admin')
        def tsWorkflowModel = TSWorkflowModel.findByModelName("projectCreateFlow")

        TSWorkflow flow1 = TSWorkflow.findByWorkflowName('flowInstance_' + project.id)
        if (flow1) {
            println "flow instance exist"
            return;
        }


        flow1 = new TSWorkflow(workflowProject: project, workflowOwner: admin, workflowName: 'flowInstance_' + project.id, workflowModel: tsWorkflowModel, workflowExcutedDate: new Date());
        flow1.save(failOnError: true)

        def modelPhase = flow1.getNextModelPhase()
        flow1.moveToModelPhase(modelPhase)
    }

    def initPayRecords() {
        String _date = "2013-01-01"
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd")
        Date payDate = sf.parse(_date)

        def bankAccount = BankAccount.findByBankName("工商银行");
        def project = TSProject.findByName("project1")
        def fund = Fund.findByFundName('fund1')

        PayRecord payRecord1 = new PayRecord(payDate: payDate, amount: 200000, payType: "borrow", bankAccount: bankAccount, project: project, fund: fund)
        payRecord1.save(failOnError: true)

        String _date2 = "2012-01-01"
        Date payDate2 = sf.parse(_date)
        PayRecord payRecord2 = new PayRecord(payDate: payDate2, amount: 400000, payType: "borrow", bankAccount: bankAccount, project: project, fund: fund)
        payRecord2.save(failOnError: true)

        String _date3 = "2011-01-01"
        Date payDate3 = sf.parse(_date)
        PayRecord payRecord3 = new PayRecord(payDate: payDate3, amount: 600000, payType: "borrow", bankAccount: bankAccount, project: project, fund: fund)
        payRecord3.save(failOnError: true)

    }

    def initCompany={
        //合伙人信息
        FundCompanyInformation fundCompanyInformation = new FundCompanyInformation();
        fundCompanyInformation.companyName = "金赛银";
        fundCompanyInformation.corporate   = "金赛银";
        fundCompanyInformation.address     = "深圳南山";
        fundCompanyInformation.province     = "广东";
        fundCompanyInformation.city     = "深圳";
        fundCompanyInformation.area     = "深圳";
        fundCompanyInformation.telephone     = "18811110000";
        fundCompanyInformation.companyNickName     = "金赛银";
        fundCompanyInformation.companyDescription     = "金融";
        fundCompanyInformation.responsiblePerson     = "草泥马";
        fundCompanyInformation.fax     = "2010-08-08";
        fundCompanyInformation.status     = "1111";
        fundCompanyInformation.remark     = "2010-08-08创建";
        fundCompanyInformation.groupCompany = null;
        fundCompanyInformation.companyType  = TypeConfig.findByTypeAndMapValue(6,1);;
        fundCompanyInformation.protocolTemplate  = 0;
        fundCompanyInformation.hhrpx = "2,3";
        fundCompanyInformation.save(failOnError: true);


        //银行信息
        BankAccount bankAccount = new BankAccount();
        bankAccount.bankName = "工商银行";
        bankAccount.bankOfDeposit = "某条街的工商银行";
        bankAccount.accountName = "金赛银1";
        bankAccount.account = "92654684613";
        bankAccount.defaultAccount = true;
        TypeConfig purpose = TypeConfig.findByTypeAndMapValue(7,1);
        bankAccount.purpose = purpose;
        bankAccount.save(failOnError: true);
        fundCompanyInformation.addToBankAccount(bankAccount);
        fundCompanyInformation.save(failOnError: true);

        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.bankName = "平安银行";
        bankAccount2.bankOfDeposit = "某条街的平安银行";
        bankAccount2.accountName = "金赛银2";
        bankAccount2.account = "9855135485544";
        bankAccount2.defaultAccount = false;
        bankAccount2.purpose = purpose;
        bankAccount2.save(failOnError: true);
        fundCompanyInformation.addToBankAccount(bankAccount2);
        fundCompanyInformation.save(failOnError: true);

        BankAccount bankAccount3 = new BankAccount();
        bankAccount3.bankName = "农业银行";
        bankAccount3.bankOfDeposit = "某条街的农业银行";
        bankAccount3.accountName = "金赛银3";
        bankAccount3.account = "98854715324";
        bankAccount3.defaultAccount = false;
        bankAccount3.purpose = purpose;
        bankAccount3.save(failOnError: true);
        fundCompanyInformation.addToBankAccount(bankAccount3);
        fundCompanyInformation.save(failOnError: true);

    }


    def initFund_Company_Relate={
        FundCompanyInformation fundCompanyInformation = FundCompanyInformation.findByCompanyName("金赛银")
        Fund fund = Fund.findByFundName('fund1');
        fundCompanyInformation.addToFunds(fund);
        fundCompanyInformation.save(failOnError: true)
    }
}
