package com.jsy.project

import com.jsy.auth.User
import com.jsy.fundObject.Fund
import com.jsy.system.UploadFile

/**
 * 基金项目，这里记录了项目的基本信息，以及创建项目流程的所有业务数据
 * 
 * 创建项目流程: gatherInfo gatherOA research researchOA meeting otherEA addCompany makeContact makeContactOA
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
    String fundNames            //关联基金名称，格式：fund1,fund2,fund3
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

    //步骤2   addCompany

    //步骤3   makeContact

    //步骤3.1 makeContactOA
    String makeContactOAStatus = "working"



    static hasMany = [
        /*****公共属性*****/
        projectWorkflows : TSWorkflow,  //创建流程
        funds: Fund,                    //关联基金
        stockRights: StockRight,        //股权信息

        /****步骤1.1 gatherInfo****/
        othersFiles:TSFlowFile,         //其他文件

        /****步骤1.3 research****/
        researchOthersFiles:TSFlowFile, //其他文件

        /****步骤1.5 research****/
        meetingOthersFiles:TSFlowFile,  //其他文件

        /****步骤1.6 otherEA****/
        thirdPartyOthersFiles:TSFlowFile,  //其他文件
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
    ]

    static constraints = {
        name blank: false, unique: true
        pdesc nullable: true
        creatorName nullable: true
        ownerName nullable: true
        currentStageName nullable: true
        currentStageEn nullable: true
        fundNames nullable: true

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

}
