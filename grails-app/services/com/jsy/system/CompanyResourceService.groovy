package com.jsy.system

import org.grails.jaxrs.provider.DomainObjectNotFoundException

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
