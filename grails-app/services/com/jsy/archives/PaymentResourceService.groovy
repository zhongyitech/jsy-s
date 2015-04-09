package com.jsy.archives

import com.jsy.fundObject.Finfo
import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class PaymentResourceService {

    //更新状态到3
    def updatePayment(Payment dto,int i){
        dto.status=i
        dto.save(failOnError: true)
    }

    def create(Payment dto) {
        dto.save()
    }

    def read(id) {
        def obj = Payment.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Payment.class, id)
        }
        obj
    }

    def readAll() {
        Payment.findAll()
    }

    def getPayments(Finfo finfo,String type) {
        return Payment.findAllByDflxAndStatusInListAndCustomerNameLikeAndZfsjBetween(type,[0,1],"%"+(finfo.keyword==null?"":finfo.keyword)+"%",finfo.startsaledate1,finfo.startsaledate2,[max: finfo.pagesize, offset: finfo.startposition,sort: "status"])
    }
    def getPaymentsTotal(Finfo finfo,String type) {
        return Payment.findAllByDflxAndStatusInListAndCustomerNameLikeAndZfsjBetween(type,[0,1],"%"+(finfo.keyword==null?"":finfo.keyword)+"%",finfo.startsaledate1,finfo.startsaledate2).size()
    }

    def getCommissions(Finfo finfo,String type) {
        return Payment.findAllByDflxAndStatusInListAndCustomerNameLikeAndZfsjBetween(type,[0,1],"%"+(finfo.keyword==null?"":finfo.keyword)+"%",finfo.startsaledate1,finfo.startsaledate2,[max: finfo.pagesize, offset: finfo.startposition,sort: "status"])
    }
    def getCommissionsTotal(Finfo finfo,String type) {
        return Payment.findAllByDflxAndStatusInListAndCustomerNameLikeAndZfsjBetween(type,[0,1],"%"+(finfo.keyword==null?"":finfo.keyword)+"%",finfo.startsaledate1,finfo.startsaledate2).size()
    }

    def update(Payment dto) {
        def obj = Payment.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Payment.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Payment.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
