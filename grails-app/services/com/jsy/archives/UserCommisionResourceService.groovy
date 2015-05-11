package com.jsy.archives

import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

/**
 * �������/ҵ����ɵĴ�����
 * 1.�Ҹ��Ѿ�֧��
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
     * ����Ϊ��֧��״̬
     * 1.ҵ����ɷ���ʱ��
     * 2.������ɶ�Ӧ��֧��ʱ��,���ݶҸ����� glPayCount�����ж�����Щ��� ,
     * ����������������ɵ�ʱд���.���������ʱ������Ҹ�����
     */
    //todo:����ɲ�ѯ���ɶҸ���ʱд��glPayCount����
    def setSuccess(UserCommision commision, Payment payment) {
        if (payment.glPayCount == null) {
            //ҵ���������豸֧��ʱ��
            commision.sjffsj = payment.yfsj
        } else {
            //������ɸ���ʱ���������֧��ʱ��
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
