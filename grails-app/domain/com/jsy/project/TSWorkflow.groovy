package com.jsy.project

import com.jsy.auth.User
import com.jsy.system.UploadFile

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
}
