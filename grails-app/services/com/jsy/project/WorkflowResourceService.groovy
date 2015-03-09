package com.jsy.project

import com.jsy.auth.Role
import com.jsy.auth.User
import com.jsy.bankConfig.BankAccount
import com.jsy.fundObject.Fund
import com.jsy.fundObject.FundCompanyInformation
import com.jsy.system.Company
import com.jsy.system.TypeConfig
import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

import java.text.DateFormat
import java.text.SimpleDateFormat

class WorkflowResourceService {

    def initRole = {
        def financialIncharger = Role.findByAuthority("FinancialIncharger")
        if(financialIncharger)
            return;

        Role role1 = new Role(authority:"FinancialIncharger", name:"财务部负责人");
        Role role2 = new Role(authority:"ProjectIncharger", name:"项目部负责人");
        Role role3 = new Role(authority:"MinistryIncharger", name:"法务部负责人");
        role1.save(failOnError: true)
        role2.save(failOnError: true)
        role3.save(failOnError: true)
    }

    def initFlowModel(){
        initRole();

        def tsWorkflowModel = TSWorkflowModel.findByModelName("projectCreateFlow")
        if(tsWorkflowModel){
            println "flow model exist!"
            return;
        }

        def admin = User.findByUsername('admin')


        TSWorkflowModel tsWorkflow = new TSWorkflowModel(modelName: "projectCreateFlow");
        tsWorkflow.save(failOnError: true)




        //SortedSet phaseParticipants;
//        static hasMany = [phaseParticipants : Role];
        def financialIncharger = Role.findByAuthority("FinancialIncharger")
        def projectIncharger = Role.findByAuthority("projectIncharger")
        def ministryIncharger = Role.findByAuthority("ministryIncharger")

        TSWorkflowModelPhase phase1 = new TSWorkflowModelPhase(phaseModel:tsWorkflow,phaseIndex:1,phaseEn:"gatherInfo",phaseName:"步骤1.1：资料采集（项目部负责并填写）");
        phase1.addToPhaseParticipants(projectIncharger)
        phase1.save(failOnError: true)
        TSWorkflowModelPhase phase2 = new TSWorkflowModelPhase(phaseModel:tsWorkflow,phaseIndex:2,phaseEn:"gatherOA",phaseName:"步骤1.2：资料评判——OA审核");
//        phase2.addToPhaseParticipants(projectIncharger)
        phase2.save(failOnError: true)
        TSWorkflowModelPhase phase3 = new TSWorkflowModelPhase(phaseModel:tsWorkflow,phaseIndex:3,phaseEn:"research",phaseName:"步骤1.3：现场考察（方案确定）（项目部负责发起申请，法务部，财务部配合）");
        phase3.addToPhaseParticipants(projectIncharger)
        phase3.save(failOnError: true)
        TSWorkflowModelPhase phase4 = new TSWorkflowModelPhase(phaseModel:tsWorkflow,phaseIndex:4,phaseEn:"researchOA",phaseName:"步骤1.4：现场考察——OA审核");
//        phase4.addToPhaseParticipants(projectIncharger)
        phase4.save(failOnError: true)
        TSWorkflowModelPhase phase5 = new TSWorkflowModelPhase(phaseModel:tsWorkflow,phaseIndex:5,phaseEn:"meeting",phaseName:"步骤1.5：投决会（项目部负责发起申请，法务部，财务部配合）");
        phase5.addToPhaseParticipants(projectIncharger)
        phase5.save(failOnError: true)
        TSWorkflowModelPhase phase6 = new TSWorkflowModelPhase(phaseModel:tsWorkflow,phaseIndex:6,phaseEn:"otherEA",phaseName:"步骤1.6：第三方法律机构（项目部负责发起申请，法务部，财务部配合）");
        phase6.addToPhaseParticipants(projectIncharger)
        phase6.save(failOnError: true)
        TSWorkflowModelPhase phase8 = new TSWorkflowModelPhase(phaseModel:tsWorkflow,phaseIndex:8,phaseEn:"addCompany",phaseName:"步骤2：添加有限合伙企业（项目部负责发起申请，法务部，财务部配合）");
        phase8.addToPhaseParticipants(projectIncharger)
        phase8.save(failOnError: true)
        TSWorkflowModelPhase phase9 = new TSWorkflowModelPhase(phaseModel:tsWorkflow,phaseIndex:9,phaseEn:"makeContact",phaseName:"步骤3：项目合同——选择预发行基金以及录入合同资料（项目部负责发起申请，法务部，财务部配合）");
        phase9.addToPhaseParticipants(projectIncharger)
        phase9.save(failOnError: true)
        TSWorkflowModelPhase phase10=new TSWorkflowModelPhase(phaseModel:tsWorkflow,phaseIndex:10,phaseEn:"makeContactOA",phaseName:"步骤3.1：项目合同——OA审核");
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
        String accessor="zhangj"
        SpecailAccess sa1 = new SpecailAccess(fromDate: fromDate, toDate:toDate, accessor:accessor, projectId:1);
        sa1.save(failOnError: true)
        SpecailAccess sa2 = new SpecailAccess(fromDate: fromDate, toDate:toDate, accessor:accessor, projectId:2, phaseEn:"gatherInfoBean");
        sa2.save(failOnError: true)

    }

    def initFund(){
        new Fund(fundName:'fund1',fundNo:'F001',startSaleDate:new Date(),status:TypeConfig.findByTypeAndMapValue(1,2),owner:'张三',memo:'备注').save(failOnError: true)
        new Fund(fundName:'fund2',fundNo:'F002',startSaleDate:new Date(),status:TypeConfig.findByTypeAndMapValue(1,2),owner:'张三',memo:'备注').save(failOnError: true)
        new Fund(fundName:'fund3',fundNo:'F002',startSaleDate:new Date(),status:TypeConfig.findByTypeAndMapValue(1,2),owner:'张三',memo:'备注').save(failOnError: true)
    }

    def initCompany(){
        def admin  = User.findByUsername("admin")
        def fund1  = Fund.findByFundName("fund1")
        def fund2  = Fund.findByFundName("fund2")
        def fund3  = Fund.findByFundName("fund3")

        //目的
        TypeConfig typeConfig1 = new TypeConfig(type: 6,mapName:"收款",mapValue: 1)
        typeConfig1.save(failOnError: true)
        TypeConfig typeConfig2 = new TypeConfig(type: 6,mapName:"汇款",mapValue: 2)
        typeConfig2.save(failOnError: true)
        TypeConfig typeConfig3 = new TypeConfig(type: 6,mapName:"国庆电商收款",mapValue: 3)
        typeConfig3.save(failOnError: true)
        BankAccount bankAccount1 = new BankAccount(
                bankName:"光大银行",
                bankOfDeposit:"黎平街支行",
                accountName:"刘先生",
                account:"21415212",
                defaultAccount:false,
                purpose:typeConfig1
        )
        bankAccount1.save(failOnError: true)
        BankAccount bankAccount2 = new BankAccount(
                bankName:"平安银行",
                bankOfDeposit:"和哦支行",
                accountName:"李先生",
                account:"53462",
                defaultAccount:false,
                purpose:typeConfig2
        )
        bankAccount2.save(failOnError: true)
        BankAccount bankAccount3 = new BankAccount(
                bankName:"人民银行",
                bankOfDeposit:"三个支行",
                accountName:"和先生",
                account:"1623623",
                defaultAccount:true,
                purpose:typeConfig3
        )
        bankAccount3.save(failOnError: true)

        BankAccount bankAccount4 = new BankAccount(
                bankName:"人民银行",
                bankOfDeposit:"三个支行",
                accountName:"chen先生",
                account:"34634",
                defaultAccount:true,
                purpose:typeConfig3
        )
        bankAccount4.save(failOnError: true)
        BankAccount bankAccount5 = new BankAccount(
                bankName:"人民银行",
                bankOfDeposit:"三个支行",
                accountName:"tian先生",
                account:"54643",
                defaultAccount:true,
                purpose:typeConfig3
        )
        bankAccount5.save(failOnError: true)

        FundCompanyInformation company1 = new FundCompanyInformation(
                companyName:"洞庭湖",
        );
        company1.addToFunds(fund1)
        company1.addToBankAccount(bankAccount1)
        company1.addToBankAccount(bankAccount2)
        company1.addToBankAccount(bankAccount3)
        company1.save(failOnError: true)

        FundCompanyInformation company2 = new FundCompanyInformation(
                companyName:"财经阳",
        );
        company2.addToFunds(fund2)
        company2.addToBankAccount(bankAccount4)
        company2.save(failOnError: true)

        FundCompanyInformation company3 = new FundCompanyInformation(
                companyName:"广州白云机场",
        );
        company3.addToFunds(fund3)
        company3.addToBankAccount(bankAccount5)
        company3.save(failOnError: true)
    }

    def createProjects(){
        def fundCompanyInformation =FundCompanyInformation.findByCompanyName("洞庭湖")
        def fund1  = Fund.findByFundName("fund1")
        def admin = User.findByUsername('admin') ?: new User(
                username: 'admin',
                password: 'admin',
                chainName: "张三",
                enabled: true).save(failOnError: true)

        (1..6).each { i ->
            TSProject.findByName('project'+i) ?: new TSProject(
                    name: 'project'+i,
                    projectDealer: 'dealer_'+i,
                    projectOwner: admin,
                    creator: admin,
            ).save(failOnError: true)
        }

        (7..13).each { i ->
            TSProject.findByName('project' + i) ?: new TSProject(
                    name: 'project' + i,
                    projectDealer: 'dealer_' + i,
                    projectOwner: admin,
                    creator: admin,
                    company: fundCompanyInformation,
                    fund: fund1
            ).save(failOnError: true)
        }
    }

    @Transactional
    def createFlow(def projectid){
        def project = TSProject.get(projectid)
        if(!project)
            return;
        def admin = User.findByUsername('admin')
        def tsWorkflowModel = TSWorkflowModel.findByModelName("projectCreateFlow")

        TSWorkflow flow1 = TSWorkflow.findByWorkflowName('flowInstance_'+project.id)
        if(flow1){
            println "flow instance exist"
            return;
        }


        flow1 = new TSWorkflow(workflowProject:project, workflowOwner: admin, workflowName:'flowInstance_'+project.id,workflowModel:tsWorkflowModel,workflowExcutedDate:new Date());
        flow1.save(failOnError: true)

        def modelPhase = flow1.getNextModelPhase()
        flow1.moveToModelPhase(modelPhase)
    }
}
