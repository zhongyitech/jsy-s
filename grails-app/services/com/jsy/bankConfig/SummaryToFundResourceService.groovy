package com.jsy.bankConfig

import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class SummaryToFundResourceService {

    def create(SummaryToFund dto) {
        dto.save()
    }

    def read(id) {
        def obj = SummaryToFund.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(SummaryToFund.class, id)
        }
        obj
    }

    def readAll() {
        SummaryToFund.findAll()
    }

    def update(SummaryToFund dto) {
        def obj = SummaryToFund.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(SummaryToFund.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = SummaryToFund.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
