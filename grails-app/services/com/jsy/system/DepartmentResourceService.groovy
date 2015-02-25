package com.jsy.system

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class DepartmentResourceService {

    def create(Department dto) {
        dto.save()
    }

    def read(id) {
        def obj = Department.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Department.class, id)
        }
        obj
    }

    def readAll() {
        Department.findAll()
    }

    def update(Department dto) {
        def obj = Department.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Department.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Department.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
