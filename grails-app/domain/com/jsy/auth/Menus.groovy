package com.jsy.auth
/**
 * 菜单
 */
class Menus {

    //菜单名称
    String name
    //菜单标题
    String title
    //页面名称
//    String pageName
    //对应url
    String url
    //父菜单id  没有值为0
    Long parentId=0

    static constraints = {
    }
}
