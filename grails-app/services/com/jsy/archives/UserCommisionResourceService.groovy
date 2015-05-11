package com.jsy.archives

import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

/**
 * 管理提成/业务提成的处理类
 * 1.兑付已经支付
 */
@Transactional(rollbackFor = Throwable.class)
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

    /**
     * 设置为已支付状态
     * 1.业务提成发放时间
     * 2.管理提成对应的支付时间,根据兑付单的 glPayCount参数判断是哪些提成 ,
     * 此数据是在生成提成单时写入的.在提成申请时拷贝入兑付单中
     */
    //todo:在提成查询生成兑付单时写入glPayCount参数
    def setSuccess(UserCommision commision, Payment payment) {
        if (payment.glPayCount == null) {
            //业务管理提成设备支付时间
            commision.sjffsj = payment.yfsj
        } else {
            //管理提成根据时间参数设置支付时间
            if (payment.glPayCount == commision.glffsj2) {
                commision.real_glffsj2 = payment.yfsj
            }
            if (payment.glPayCount == commision.glffsj3) {
                commision.real_glffsj3 = payment.yfsj
            }
        }
        commision.yhzh = payment.zh
        commision.khh = payment.khh
        commision.save(failOnError: true)
    }
}
