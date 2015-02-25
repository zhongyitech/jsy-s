package com.jsy.auth
/**
 * 角色资源对应关系表 包含所含有的权限
 */
class ResourceRole {

    Role role
    Resource resource

    static hasMany = [operations:Operation,propertys:Property]

    static constraints = {
    }
}
