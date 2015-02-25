package com.jsy.system
/**
 * 模块表
 */
class Module {

    //模块名
    String moduleName
    //父模块
    Long parentID
    //别名
    String aliasName
    //是否显示
    boolean isShow=true
    //排序
    String sort
    //前置图标
    String icon
    //链接
    String url
    //描述
    String memo
    //状态
    String status

    static constraints = {
    }
}
