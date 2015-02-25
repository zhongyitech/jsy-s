package com.jsy.system

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class ToDoTaskResourceService {

    def create(ToDoTask dto) {
        dto.save()
    }

    def read(id) {
        def obj = ToDoTask.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(ToDoTask.class, id)
        }
        obj
    }

    def readAll() {
        ToDoTask.findAll()
    }

    def update(ToDoTask dto) {
        def obj = ToDoTask.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(ToDoTask.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = ToDoTask.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
