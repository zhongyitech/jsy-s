package com.jsy.project

import com.jsy.auth.User
import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * 特殊时间段的访问
 */
class SpecailAccess implements Serializable{
    Date fromDate
    Date toDate
    int accessor    //用户id
    int projectId   //项目id
    int phaseIndex;
    String phaseEn

    static constraints = {
        fromDate nullable: true
        toDate   nullable: true
        accessor nullable: true
        projectId nullable: true
        phaseIndex nullable: true
        phaseEn nullable: true
    }


    static mapping = {
        id composite: ['projectId', 'accessor']
    }

    boolean equals(other) {
        if (!(other instanceof SpecailAccess)) {
            return false
        }

        other.projectId == projectId && other.accessor == accessor
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        builder.append projectId
        builder.append accessor
        builder.toHashCode()
    }
}
