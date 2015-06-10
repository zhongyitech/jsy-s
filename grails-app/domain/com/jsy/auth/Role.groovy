package com.jsy.auth

class Role {

    String authority
    String name
    String description
    //是否系统预置角色（不可删除 ）
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
