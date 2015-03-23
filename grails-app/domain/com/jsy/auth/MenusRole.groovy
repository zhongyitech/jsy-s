package com.jsy.auth
/**
 * 角色菜单权限关系
 */
class MenusRole {
    //角色
    Role role
    //菜单
    Menus menus
    //是否有权限
    boolean  visible

    static constraints = {
    }
}
