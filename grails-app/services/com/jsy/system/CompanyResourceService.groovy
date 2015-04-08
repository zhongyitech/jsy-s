package com.jsy.system

import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class CompanyResourceService {

    def create(Company dto) {
        dto.save()
    }

    def read(id) {
        def obj = Company.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Company.class, id)
        }
        obj
    }

    def readAll() {
        Company.findAll()
    }

    def update(Company dto) {
        def obj = Company.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Company.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Company.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
