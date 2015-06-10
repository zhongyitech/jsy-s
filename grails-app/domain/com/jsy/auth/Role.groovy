package com.jsy.auth

class Role {

    String authority
    String name
    String description
    //�Ƿ�ϵͳԤ�ý�ɫ������ɾ�� ��
    boolean isDefault = false
    static mapping = {
        cache true
    }

    def beforeInsert() {
        if (!authority) {
            authority = UUID.randomUUID().toString()
        }
    }

    static constraints = {
        name blank: false, unique: true
        authority nullable: true, unique: true
        description nullable: true
    }
}
