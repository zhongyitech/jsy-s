package com.jsy.project

import com.jsy.auth.Role

class TSWorkflowModel {

    String modelName='';


    SortedSet modelPhases;
    static hasMany = [modelCreators : Role, modelPhases: TSWorkflowModelPhase];

    static constraints = {
        modelName blank:true, maxSize:1000
    }
}