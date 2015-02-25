package com.jsy.system
/**
 * 菜单
 */
class Menu {

    //菜单名
    String menuName
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

    //所属模块
    static belongsTo = [module:Module]

    static constraints = {
    }
}
