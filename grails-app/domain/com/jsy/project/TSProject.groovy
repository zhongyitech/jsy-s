package com.jsy.project

import com.jsy.auth.User
import com.jsy.fundObject.Fund
import com.jsy.fundObject.FundCompanyInformation

/**
 * 基金项目，这里记录了项目的基本信息，以及创建项目流程的所有业务数据
 * 
 * 创建项目流程: gatherInfo gatherOA research researchOA meeting otherEA  makeContact makeContactOA
 */
class TSProject {

    /*****普通字段******/
    String name              //项目名称
    String projectDealer     //项目方
    User projectOwner;       //项目负责人
    User creator;            //创建者
    String pdesc;            //备注

    boolean isEnded=false;   //创建流程是否结束
    boolean archive=false;   //项目是否归档

    String director          //董事
    String supervisor        //监事
    String stockStructure    //股干人员架构
    String debt              //债务
    String assets            //资产


    /*****冗余字段*******/
    String currentStageName;    //当前阶段名称
    String currentStageEn;      //当前阶段英文名
    String fundName            //关联基金名称
    String creatorName;         //创建者名称
    String ownerName;           //负责人名称

    /**common字段**/
    Date dateCreated
    Date lastUpdated


    /*******下面是流程的数据*******/
    //步骤1.1 gatherInfo
    TSFlowFile certificateFile   //项目证照
    TSFlowFile debtFile          //债务报告
    TSFlowFile financialFile     //财务报表
    TSFlowFile toPublicFile      //对公批文
    TSFlowFile businessPlanFile  //商务计划书

    //步骤1.2 gatherOA
    String gatherOAStatus = "working"

    //步骤1.3 research
    TSFlowFile lawTransfer       //法律进调
    TSFlowFile projectTransfer   //项目进调
    TSFlowFile financialReport   //财务报告

    //步骤1.4 researchOA
    String researchOAStatus = "working"

    //步骤1.5 meeting
    TSFlowFile meetingRecord       //会议纪要

    //步骤1.6 otherEA
    TSFlowFile thirdPartyFile


    //步骤2   makeContact
    FundCompanyInformation company       //有限合伙
    Fund fund                            //关联基金
    BigDecimal manage_per                //管理费率
    BigDecimal community_per             //渠道费率
    BigDecimal penalty_per               //违约金率
    BigDecimal borrow_per                //借款率
    BigDecimal interest_per              //本金的年利率
    BigDecimal year1                     //期限：约定
    BigDecimal year2                     //期限：缓冲
    String interestType                  //利息计算方式

    //步骤2.1 makeContactOA
    String makeContactOAStatus = "working"



    static hasMany = [
        /*****公共属性*****/
        projectWorkflows : TSWorkflow,  //创建流程
        stockRights: StockRight,        //股权信息

        /****步骤1.1 gatherInfo****/
        othersFiles:TSFlowFile,         //其他文件

        /****步骤1.3 research****/
        researchOthersFiles:TSFlowFile, //其他文件

        /****步骤1.5 research****/
        meetingOthersFiles:TSFlowFile,  //其他文件

        /****步骤1.6 otherEA****/
        thirdPartyOthersFiles:TSFlowFile,  //其他文件

        /****步骤2 签署合同****/
        signers: SimpleRecord,                //签署方
        attentions: SimpleRecord,             //注意事项
        makeContactOthersFiles:TSFlowFile,    //其他文件
    ];

    static mappedBy = [
            certificateFile: "none",
            debtFile: "none",
            financialFile: "none",
            toPublicFile: "none",
            businessPlanFile: "none",
            othersFiles: "none",

            lawTransfer: "none",
            projectTransfer: "none",
            financialReport: "none",
            researchOthersFiles: "none",

            meetingRecord: "none",
            meetingOthersFiles: "none",

            thirdPartyFile: "none",
            thirdPartyOthersFiles: "none",

            makeContactOthersFiles: "none",
            attentions: "none",
            signers: "none",
    ]

    static constraints = {
        name blank: false, unique: true
        pdesc nullable: true
        creatorName nullable: true
        ownerName nullable: true
        currentStageName nullable: true
        currentStageEn nullable: true
        fundName nullable: true

        certificateFile nullable: true
        debtFile nullable: true
        financialFile nullable: true
        toPublicFile nullable: true
        businessPlanFile nullable: true


        director nullable: true
        supervisor nullable: true
        stockStructure nullable: true
        debt nullable: true
        assets nullable: true

        gatherOAStatus : ["working", "complete", "reject", "fallback"]

        lawTransfer nullable: true
        projectTransfer nullable: true
        financialReport nullable: true

        researchOAStatus : ["working", "complete", "reject", "fallback"]
        makeContactOAStatus : ["working", "complete", "reject", "fallback"]

        meetingRecord nullable: true

        thirdPartyFile nullable: true
        thirdPartyOthersFiles nullable: true

        company nullable: true
        fund nullable: true
        interestType : ["singleCount", "costCount", "dayCount"] //单利  复利  日复利


        manage_per nullable: true
        community_per nullable: true
        penalty_per nullable: true
        borrow_per nullable: true
        interest_per nullable: true
        year1 nullable: true
        year2 nullable: true
        interestType  nullable: true

    }

    def beforeInsert() {
        if (fund) {
            fundName = fund.fundName
        }
        if (projectOwner) {
            ownerName = projectOwner.username
        }
        if (creator) {
            creatorName = creator.username
        }
    }


    def beforeUpdate() {
        if (fund) {
            fundName = fund.fundName
        }else{
            fundName = ""
        }
    }

    public TSWorkflow getProjectWorkflow(){
        if(!projectWorkflows || projectWorkflows.size()==0)
            return null
        return projectWorkflows[0];
    }

    def iterateWithPhase(handlePhase){
        getProjectWorkflow().workflowPhases.each {phase->
            handlePhase(handlePhase,this)
        }
    }

    def getProjectSimpleInfo(){
        def rtn = [
                id:id,
                name:name,
                projectDealer:projectDealer,
                currentStageName:currentStageName,
                currentStageEn:currentStageEn,
                fundName:fundName,
                creatorName:creatorName,
                ownerName:ownerName,
                dateCreated:dateCreated,
                lastUpdated:lastUpdated,
                director:director,
                supervisor:supervisor,
                stockStructure:stockStructure,
                debt:debt,
                assets:assets,
                isEnded:isEnded,
                archive:archive,
                pdesc:pdesc,

                manage_per:manage_per,               //管理费率
                community_per:community_per,             //渠道费率
                penalty_per:penalty_per,               //违约金率
                borrow_per:borrow_per,               //借款率
                interest_per:interest_per,                //本金的年利率
                year1:year1,                    //期限：约定
                year2:year2,                     //期限：缓冲
                interestType:interestType             //利息计算方式

        ]
        return rtn;
    }

}
