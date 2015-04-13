package com.jsy.auth
/**
 * 资源
 */
class Resource {

    //资源名称
    String name
    //资源对应对象名
    String objectName
    SortedSet propertys
    SortedSet operations
    static hasMany = [operations:Operation,propertys:Property]
    static constraints = {
    }
}
