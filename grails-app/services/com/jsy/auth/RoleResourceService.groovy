package com.jsy.auth

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class RoleResourceService {

    def create(Role dto) {
        dto.save()
    }

    def read(id) {
        def obj = Role.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Role.class, id)
        }
        obj
    }

    def readAll() {
        Role.findAll()
    }

    def update(Role dto) {
        def obj = Role.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Role.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Role.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
