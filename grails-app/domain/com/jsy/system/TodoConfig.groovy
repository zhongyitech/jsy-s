package com.jsy.system

import com.jsy.auth.Role

/**
 * 待办事项配置
 */
class TodoConfig {

    //对应模块标识
    int mkbs
    //模块名称
    String mkmc
    //模块操作角色
    Role cljs
    //模块地址
    String url

    static constraints = {
    }
}
