package com.jsy.project

import com.jsy.auth.Role

class TSWorkflowModelPhase implements Comparable {

    int phaseIndex;//阶段顺序
    String phaseName='';
    String phaseEn;
    String phaseType='';//一般情况下为空

    static belongsTo = [phaseModel : TSWorkflowModel];


//    SortedSet phaseParticipants;
    static hasMany = [phaseParticipants : Role];


    Date dateCreated;
    Date lastUpdated;

    static constraints = {
        phaseName blank:true, maxSize:1000
        phaseType blank:true, maxSize:1000

        phaseModel nullable: false
    }

    static mapping = {
    }

    @Override
    int compareTo(obj) {
        phaseIndex.compareTo(obj?.phaseIndex);
    }
}
