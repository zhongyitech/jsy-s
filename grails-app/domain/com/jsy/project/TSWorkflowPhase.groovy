package com.jsy.project

import com.jsy.auth.User
import com.jsy.auth.Role

class TSWorkflowPhase implements Comparable {

    int phaseIndex;// copy from model
    String phaseName='';// copy from model
    String phaseEn;
    String phaseType='';
    String phaseComment='';

    User phaseExe;
    boolean phaseFinished=false;
    boolean phaseRejected=false;

    Date phaseExcutedDate;

    static belongsTo = [phaseWorkflow : TSWorkflow];

    static hasMany = [phaseParticipants : Role];

    static constraints = {
        phaseName blank:true, maxSize:1000
        phaseType blank:true, maxSize:1000
        phaseComment blank:true, maxSize:1000

        phaseExe nullable: true

        phaseWorkflow nullable: false

        phaseExcutedDate nullable:true
    }

    @Override
    int compareTo(obj) {
        phaseIndex.compareTo(obj.phaseIndex);
    }

}

