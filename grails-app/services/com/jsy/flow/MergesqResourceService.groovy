package com.jsy.flow

import com.jsy.archives.INVESTMENT_STATUS
import com.jsy.archives.InvestmentArchives
import com.jsy.utility.INVESTMENT_SPEICAL_STATUS
import org.grails.jaxrs.provider.DomainObjectNotFoundException

class MergesqResourceService {

    def create(Mergesq dto) {
        InvestmentArchives investmentArchives = InvestmentArchives.findByContractNum(dto.htbh)
        investmentArchives.dazt = INVESTMENT_SPEICAL_STATUS.UNION.value
        investmentArchives.status = INVESTMENT_STATUS.New.value
        investmentArchives.save(failOnError: true)
        dto.save(failOnError: true)
    }

    def read(id) {
        def obj = Mergesq.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Mergesq.class, id)
        }
        obj
    }

    def readAll() {
        Mergesq.findAll()
    }

    def update(Mergesq dto) {
        def obj = Mergesq.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Mergesq.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Mergesq.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
