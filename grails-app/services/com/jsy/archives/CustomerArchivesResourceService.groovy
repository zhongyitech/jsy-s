package com.jsy.archives

import com.jsy.customerObject.Customer
import org.grails.jaxrs.provider.DomainObjectNotFoundException

class CustomerArchivesResourceService {

    def create(CustomerArchives dto) {
        dto.save()
    }

    def read(id) {
        def obj = CustomerArchives.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(CustomerArchives.class, id)
        }
        obj
    }

    def readAll() {
        CustomerArchives.findAll()
    }

    def update(CustomerArchives dto) {
        def obj = CustomerArchives.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(CustomerArchives.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = CustomerArchives.get(id)
        if (obj) {
            obj.delete()
        }
    }

    def CopyCustom(Customer cus){


        return cus
    }
}
