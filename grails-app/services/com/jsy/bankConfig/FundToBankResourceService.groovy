package com.jsy.bankConfig

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class FundToBankResourceService {

    def create(FundToBank dto) {
        dto.save()
    }

    def read(id) {
        def obj = FundToBank.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(FundToBank.class, id)
        }
        obj
    }

    def readAll() {
        FundToBank.findAll()
    }

    def update(FundToBank dto) {
        def obj = FundToBank.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(FundToBank.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = FundToBank.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
