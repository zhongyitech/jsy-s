package com.jsy.system

import com.jsy.auth.Role
import com.jsy.auth.User

/**
 * 待办事项
 */
class ToDoTask {

    //所属模块
    String ssmk
    //处理角色
    Role cljs
    //处理时间
    Date clsj
    //处理状态  0为未处理
    int status
    //任务创建时间
    Date cjsj
    //处理人
    User clr
    //处理地址
    String url

    String bz

    static ToDoTask create(String ssmk, Role cljs, String url) {
        def instance = new ToDoTask(ssmk: ssmk, cljs: cljs, url: url, cjsj: new Date(), status: 0)
        instance.save(failOnError: true)
        instance
    }

    static constraints = {
        clsj nullable: true
        clr nullable: true
        bz nullable: true
    }
}
