package com.jsy.project

import com.jsy.system.Company
import org.apache.commons.lang.builder.HashCodeBuilder

class ProjectCompany implements Serializable{
    TSProject project
    Company company

    boolean equals(other) {
        if (!(other instanceof ProjectCompany)) {
            return false
        }

        other.project?.id == project?.id &&
                other.company?.id == company?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (company) builder.append(company.id)
        if (project) builder.append(project.id)
        builder.toHashCode()
    }
    static constraints = {
    }

    static mapping = {
        id composite: ['project', 'company']
        version false
    }
}
