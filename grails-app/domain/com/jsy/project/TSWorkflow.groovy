package com.jsy.project

import com.jsy.auth.User
import com.jsy.system.UploadFile
import grails.transaction.Transactional

class TSWorkflow {

    User workflowOwner;
    String workflowName='';
    TSWorkflowModel workflowModel;
    TSWorkflowPhase workflowCurrentPhase;
    Date workflowExcutedDate;

//    SortedSet workflowPhases;


    static belongsTo = [workflowProject : TSProject];

    static hasMany = [workflowPhases: TSWorkflowPhase];


    Date dateCreated;
    Date lastUpdated;

    static constraints = {

        workflowProject nullable:false
        workflowModel nullable:true


        workflowOwner nullable:false

        workflowName blank: false, unique: true

        workflowCurrentPhase nullable:true

        workflowExcutedDate nullable:true
    }

    def getNextModelPhase(){
        if(workflowCurrentPhase){
            return workflowModel.modelPhases.find{it.phaseIndex==(workflowCurrentPhase.phaseIndex+1)};
        }else{
            return workflowModel.modelPhases.find{it.phaseIndex== 1};
        }
    }


    def getGatherInfoPhase(){
        def phase =  workflowModel.modelPhases.find{it.phaseEn== 'gatherInfo'};
        return phase
    }

    def getGatherInfo(){
        def phase =  workflowPhases.find{it.phaseEn== 'gatherInfo'};
        return phase
    }

    def getGatherOAPhase(){
        def phase = workflowModel.modelPhases.find{it.phaseEn== 'gatherOA'};
        return phase
    }

    def getGatherOA(){
        def phase = workflowPhases.find{it.phaseEn== 'gatherOA'};
        return phase
    }

    def getResearchPhase(){
        def phase = workflowModel.modelPhases.find{it.phaseEn== 'research'};
        return phase
    }

    def getResearch(){
        def phase = workflowPhases.find{it.phaseEn== 'research'};
        return phase
    }

    def getResearchOAPhase(){
        def phase = workflowModel.modelPhases.find{it.phaseEn== 'researchOA'};
        return phase
    }

    def getResearchOA(){
        def phase = workflowPhases.find{it.phaseEn== 'researchOA'};
        return phase
    }

    def getMeetingPhase(){
        def phase = workflowModel.modelPhases.find{it.phaseEn== 'meeting'};
        return phase
    }

    def getMeeting(){
        def phase = workflowPhases.find{it.phaseEn== 'meeting'};
        return phase
    }

    def getOtherEAPhase(){
        def phase = workflowModel.modelPhases.find{it.phaseEn== 'otherEA'};
        return phase
    }

    def getOtherEA(){
        def phase = workflowPhases.find{it.phaseEn== 'otherEA'};
        return phase
    }

    def getAddCompanyPhase(){
        def phase = workflowModel.modelPhases.find{it.phaseEn== 'addCompany'};
        return phase
    }

    def getAddCompany(){
        def phase = workflowPhases.find{it.phaseEn== 'addCompany'};
        return phase
    }

    def getMakeContactPhase(){
        def phase = workflowModel.modelPhases.find{it.phaseEn== 'makeContact'};
        return phase
    }

    def getMakeContact(){
        def phase = workflowPhases.find{it.phaseEn== 'makeContact'};
        return phase
    }

    def getMakeContactOAPhase(){
        def phase = workflowModel.modelPhases.find{it.phaseEn== 'makeContactOA'};
        return phase
    }

    def getMakeContactOA(){
        def phase = workflowPhases.find{it.phaseEn== 'makeContactOA'};
        return phase
    }

    @Transactional
    def moveToModelPhase(modelPhase){
        if(!modelPhase) {
            throw new Exception("no modelPhase")
        };

        this.workflowCurrentPhase?.phaseFinished=true
        this.workflowCurrentPhase?.save(failOnError: true)

        TSWorkflowPhase workflowCurrentPhase = new TSWorkflowPhase(
                phaseIndex: modelPhase.phaseIndex,
                phaseEn: modelPhase.phaseEn,
                phaseName: modelPhase.phaseName,
                phaseWorkflow:this
        );
        modelPhase.phaseParticipants?.each{parti->
            workflowCurrentPhase.addToPhaseParticipants(parti)
        }

        if(workflowCurrentPhase.hasErrors()){
            throw new Exception(workflowCurrentPhase.getErrors())
        }
        workflowCurrentPhase.save(failOnError: true)

        //冗余设计
        workflowProject.currentStageName=workflowCurrentPhase.phaseName
        workflowProject.currentStageEn=workflowCurrentPhase.phaseEn
        workflowProject.save(failOnError: true)

        this.workflowCurrentPhase=workflowCurrentPhase
        this.addToWorkflowPhases(workflowCurrentPhase)
        if(this.hasErrors()){
            throw new Exception(this.getErrors())
        }

        this.save(failOnError: true)
    }
}
