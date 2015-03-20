package com.jsy.project

import Models.MsgModel
import com.jsy.archives.CommissionInfoResourceService
import com.jsy.auth.Role
import com.jsy.auth.User
import com.jsy.auth.UserRole
import com.jsy.bankConfig.BankAccount
import com.jsy.fundObject.Fund
import com.jsy.fundObject.FundCompanyInformation
import com.jsy.system.Company
import com.jsy.system.TypeConfig
import grails.converters.JSON
import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

import java.text.DateFormat
import java.text.SimpleDateFormat

class WorkflowResourceService {
    private static String TAG = "WorkflowResourceService ";
    CommissionInfoResourceService commissionInfoResourceService

    def initData = {
        commissionInfoResourceService.initData()

        initFund()
        initCompany()
        createProjects()
        initPayRecords();
        initRole();
        initFlowModel()
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

    def initFlowModel() {


        def tsWorkflowModel = TSWorkflowModel.findByModelName("projectCreateFlow")
        if (tsWorkflowModel) {
            println "flow model exist!"
            return;
        }

        def admin = User.findByUsername('admin')


        TSWorkflowModel tsWorkflow = new TSWorkflowModel(modelName: "projectCreateFlow");
        tsWorkflow.save(failOnError: true)

        //SortedSet phaseParticipants;
//        static hasMany = [phaseParticipants : Role];
        def financialIncharger = Role.findByAuthority("FinancialIncharger")
        def projectIncharger = Role.findByAuthority("ProjectIncharger")
        def ministryIncharger = Role.findByAuthority("MinistryIncharger")

        TSWorkflowModelPhase phase1 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 1, phaseEn: "gatherInfo", phaseName: "步骤1.1：资料采集（项目部负责并填写）");
        phase1.addToPhaseParticipants(projectIncharger)
        phase1.save(failOnError: true)
        TSWorkflowModelPhase phase2 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 2, phaseEn: "gatherOA", phaseName: "步骤1.2：资料评判——OA审核");
//        phase2.addToPhaseParticipants(projectIncharger)
        phase2.save(failOnError: true)
        TSWorkflowModelPhase phase3 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 3, phaseEn: "research", phaseName: "步骤1.3：现场考察（方案确定）（项目部负责发起申请，法务部，财务部配合）");
        phase3.addToPhaseParticipants(projectIncharger)
        phase3.save(failOnError: true)
        TSWorkflowModelPhase phase4 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 4, phaseEn: "researchOA", phaseName: "步骤1.4：现场考察——OA审核");
//        phase4.addToPhaseParticipants(projectIncharger)
        phase4.save(failOnError: true)
        TSWorkflowModelPhase phase5 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 5, phaseEn: "meeting", phaseName: "步骤1.5：投决会（项目部负责发起申请，法务部，财务部配合）");
        phase5.addToPhaseParticipants(projectIncharger)
        phase5.save(failOnError: true)
        TSWorkflowModelPhase phase6 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 6, phaseEn: "otherEA", phaseName: "步骤1.6：第三方法律机构（项目部负责发起申请，法务部，财务部配合）");
        phase6.addToPhaseParticipants(projectIncharger)
        phase6.save(failOnError: true)
        TSWorkflowModelPhase phase8 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 8, phaseEn: "addCompany", phaseName: "步骤2：添加有限合伙企业（项目部负责发起申请，法务部，财务部配合）");
        phase8.addToPhaseParticipants(projectIncharger)
        phase8.save(failOnError: true)
        TSWorkflowModelPhase phase9 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 9, phaseEn: "makeContact", phaseName: "步骤3：项目合同——选择预发行基金以及录入合同资料（项目部负责发起申请，法务部，财务部配合）");
        phase9.addToPhaseParticipants(projectIncharger)
        phase9.save(failOnError: true)
        TSWorkflowModelPhase phase10 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 10, phaseEn: "makeContactOA", phaseName: "步骤3.1：项目合同——OA审核");
//        phase10.addToPhaseParticipants(projectIncharger)
        phase10.save(failOnError: true)

        tsWorkflow.addToModelPhases(phase1)
        tsWorkflow.addToModelPhases(phase2)
        tsWorkflow.addToModelPhases(phase3)
        tsWorkflow.addToModelPhases(phase4)
        tsWorkflow.addToModelPhases(phase5)
        tsWorkflow.addToModelPhases(phase6)
        tsWorkflow.addToModelPhases(phase8)
        tsWorkflow.addToModelPhases(phase9)
        tsWorkflow.addToModelPhases(phase10)
        tsWorkflow.save(failOnError: true)

        //
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date fromDate = dateFormat.parse("20150101");
        Date toDate = dateFormat.parse("20250101");
        String accessor = "zhangj"
        SpecailAccess sa1 = new SpecailAccess(fromDate: fromDate, toDate: toDate, accessor: accessor, projectId: 1);
        sa1.save(failOnError: true)
        SpecailAccess sa2 = new SpecailAccess(fromDate: fromDate, toDate: toDate, accessor: accessor, projectId: 2, phaseEn: "gatherInfoBean");
        sa2.save(failOnError: true)

    }

    def initFund() {

        Fund.findByFundName('fund1') ?: new Fund(fundName: 'fund1', fundNo: 'F001', startSaleDate: new Date(), status: TypeConfig.findByTypeAndMapValue(1, 2), owner: '张三', memo: '备注').save(failOnError: true)
        Fund.findByFundName('fund2') ?: new Fund(fundName: 'fund2', fundNo: 'F002', startSaleDate: new Date(), status: TypeConfig.findByTypeAndMapValue(1, 2), owner: '张三', memo: '备注').save(failOnError: true)
        Fund.findByFundName('fund3') ?: new Fund(fundName: 'fund3', fundNo: 'F003', startSaleDate: new Date(), status: TypeConfig.findByTypeAndMapValue(1, 2), owner: '张三', memo: '备注').save(failOnError: true)
    }

    def initCompany() {
        def admin = User.findByUsername("admin")
        def fund1 = Fund.findByFundName("fund1")
        def fund2 = Fund.findByFundName("fund2")
        def fund3 = Fund.findByFundName("fund3")

        //目的
        TypeConfig typeConfig1 = new TypeConfig(type: 6, mapName: "收款", mapValue: 1)
        typeConfig1.save(failOnError: true)
        TypeConfig typeConfig2 = new TypeConfig(type: 6, mapName: "汇款", mapValue: 2)
        typeConfig2.save(failOnError: true)
        TypeConfig typeConfig3 = new TypeConfig(type: 6, mapName: "国庆电商收款", mapValue: 3)
        typeConfig3.save(failOnError: true)
        BankAccount bankAccount1 = new BankAccount(
                bankName: "光大银行",
                bankOfDeposit: "黎平街支行",
                accountName: "刘先生",
                account: "21415212",
                defaultAccount: false,
                purpose: typeConfig1
        )
        bankAccount1.save(failOnError: true)
        BankAccount bankAccount2 = new BankAccount(
                bankName: "平安银行",
                bankOfDeposit: "和哦支行",
                accountName: "李先生",
                account: "53462",
                defaultAccount: false,
                purpose: typeConfig2
        )
        bankAccount2.save(failOnError: true)
        BankAccount bankAccount3 = new BankAccount(
                bankName: "人民银行",
                bankOfDeposit: "三个支行",
                accountName: "和先生",
                account: "1623623",
                defaultAccount: true,
                purpose: typeConfig3
        )
        bankAccount3.save(failOnError: true)

        BankAccount bankAccount4 = new BankAccount(
                bankName: "人民银行",
                bankOfDeposit: "三个支行",
                accountName: "chen先生",
                account: "34634",
                defaultAccount: true,
                purpose: typeConfig3
        )
        bankAccount4.save(failOnError: true)
        BankAccount bankAccount5 = new BankAccount(
                bankName: "人民银行",
                bankOfDeposit: "三个支行",
                accountName: "tian先生",
                account: "54643",
                defaultAccount: true,
                purpose: typeConfig3
        )
        bankAccount5.save(failOnError: true)

        FundCompanyInformation company1 = new FundCompanyInformation(
                companyName: "洞庭湖",
        );
        company1.addToFunds(fund1)
        company1.addToBankAccount(bankAccount1)
        company1.addToBankAccount(bankAccount2)
        company1.addToBankAccount(bankAccount3)
        company1.save(failOnError: true)

        FundCompanyInformation company2 = new FundCompanyInformation(
                companyName: "财经阳",
        );
        company2.addToFunds(fund2)
        company2.addToBankAccount(bankAccount4)
        company2.save(failOnError: true)

        FundCompanyInformation company3 = new FundCompanyInformation(
                companyName: "广州白云机场",
        );
        company3.addToFunds(fund3)
        company3.addToBankAccount(bankAccount5)
        company3.save(failOnError: true)
    }

    def createProjects() {
        def fundCompanyInformation = FundCompanyInformation.findByCompanyName("洞庭湖")
        def fund1 = Fund.findByFundName("fund1")
        def admin = User.findByUsername('admin') ?: new User(
                username: 'admin',
                password: 'admin',
                chainName: "张三",
                enabled: true).save(failOnError: true)

        (1..6).each { i ->
            TSProject.findByName('project' + i) ?: new TSProject(
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
                    interestType: "costCount"
            ).save(failOnError: true)
        }

        (7..13).each { i ->
            TSProject.findByName('project' + i) ?: new TSProject(
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
                    interestType: "costCount",
                    company: fundCompanyInformation,
                    fund: fund1
            ).save(failOnError: true)
        }
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

        def bankAccount = BankAccount.findByBankName("光大银行");
        def project = TSProject.findByName("project7")
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

    def initFundCompanyInformation(){

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
        TypeConfig typeConfig = new TypeConfig();
        typeConfig.mapName="AAAAA";
        typeConfig.mapValue = 0;
        fundCompanyInformation.companyType  = typeConfig;
        fundCompanyInformation.protocolTemplate  = 0;
        fundCompanyInformation.hhrpx = "2,3";

        fundCompanyInformation.save(failOnError: true);
        if(fundCompanyInformation.hasErrors()){
            println(fundCompanyInformation.getErrors());
        }

        FundCompanyInformation partnerA = new FundCompanyInformation();
        partnerA.companyName = "金赛银A";
        partnerA.corporate   = "金赛银A";
        partnerA.address     = "深圳南山";
        partnerA.province     = "广东";
        partnerA.city     = "深圳";
        partnerA.area     = "深圳";
        partnerA.telephone     = "18811110000";
        partnerA.companyNickName     = "金赛银";
        partnerA.companyDescription     = "金融";
        partnerA.responsiblePerson     = "草泥马";
        partnerA.fax     = "2010-08-08";
        partnerA.status     = "1111";
        partnerA.remark     = "2010-08-08创建";
        partnerA.groupCompany = fundCompanyInformation;
        TypeConfig typeConfigA = new TypeConfig();
        typeConfigA.mapName="AAAAA";
        typeConfigA.mapValue = 0;
        partnerA.companyType  = typeConfigA;
        partnerA.protocolTemplate  = 0;
        fundCompanyInformation.hhrpx = "";
        partnerA.save(failOnError: true);


        partnerA.addToPartner(fundCompanyInformation);

        if(partnerA.hasErrors()){
            println(partnerA.getErrors());
        }

        FundCompanyInformation partnerB = new FundCompanyInformation();
        partnerB.companyName = "金赛银A";
        partnerB.corporate   = "金赛银A";
        partnerB.address     = "深圳南山";
        partnerB.province     = "广东";
        partnerB.city     = "深圳";
        partnerB.area     = "深圳";
        partnerB.telephone     = "18811110000";
        partnerB.companyNickName     = "金赛银";
        partnerB.companyDescription     = "金融";
        partnerB.responsiblePerson     = "草泥马";
        partnerB.fax     = "2010-08-08";
        partnerB.status     = "1111";
        partnerB.remark     = "2010-08-08创建";
        partnerB.groupCompany = fundCompanyInformation;
        TypeConfig typeConfigB = new TypeConfig();
        typeConfigB.mapName="AAAAA";
        typeConfigB.mapValue = 0;
        partnerB.companyType  = typeConfigB;
        partnerB.protocolTemplate  = 0;
        fundCompanyInformation.hhrpx = "";

        partnerB.save(failOnError: true);

        partnerB.addToPartner(fundCompanyInformation);
        if(partnerB.hasErrors()){
            println(partnerB.getErrors());
        }

        BankAccount bankAccount = new BankAccount();
        bankAccount.bankName = "工商银行";
        bankAccount.bankOfDeposit = "工商银行";
        bankAccount.accountName = "金赛银";
        bankAccount.account = "1111111111111111";
        bankAccount.defaultAccount = true;

        TypeConfig purpose = TypeConfig.findByTypeAndMapValue(7,1);
        bankAccount.purpose = purpose;
        bankAccount.purpose.save(failOnError: true);

        bankAccount.save(failOnError: true);

        fundCompanyInformation.addToBankAccount(bankAccount);

        BankAccount bankAccountA = new BankAccount();
        bankAccountA.bankName = "工商银行";
        bankAccountA.bankOfDeposit = "工商银行";
        bankAccountA.accountName = "金赛银";
        bankAccountA.account = "1111111111111111";
        bankAccountA.defaultAccount = true;

        TypeConfig purposeA = TypeConfig.findByTypeAndMapValue(7,1);

        bankAccountA.purpose = purposeA;
        bankAccountA.purpose.save(failOnError: true);

        bankAccountA.save(failOnError: true);

        partnerA.addToBankAccount(bankAccount);

        BankAccount bankAccountB = new BankAccount();
        bankAccountB.bankName = "工商银行";
        bankAccountB.bankOfDeposit = "工商银行";
        bankAccountB.accountName = "金赛银";
        bankAccountB.account = "1111111111111111";
        bankAccountB.defaultAccount = true;

        TypeConfig purposeB = TypeConfig.findByTypeAndMapValue(7,1);
        bankAccountB.purpose = purposeB;
        bankAccountB.purpose.save(failOnError: true);

        bankAccountB.save(failOnError: true);

        partnerB.addToBankAccount(bankAccount);

        Fund fund = Fund.findByFundName('fund1');

        fundCompanyInformation.addToFunds(fund);
        partnerA.addToFunds(fund);
        partnerB.addToFunds(fund);
    }
}
