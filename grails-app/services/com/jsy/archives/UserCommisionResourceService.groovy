package com.jsy.archives

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class UserCommisionResourceService {

    def create(UserCommision dto) {
        dto.save()
    }

    def read(id) {
        def obj = UserCommision.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(UserCommision.class, id)
        }
        obj
    }

    def readAll() {
        UserCommision.findAll()
    }

    def update(UserCommision dto) {
        def obj = UserCommision.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(UserCommision.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = UserCommision.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
