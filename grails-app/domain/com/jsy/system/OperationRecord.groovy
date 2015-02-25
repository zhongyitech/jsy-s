package com.jsy.system
/**
 * 操作记录
 */
class OperationRecord {
    //操作人
    String czr
    //操作时间
    Date czsj
    //访问模块
    String url
    //请求方式
    String method
    //请求参数
    String params
    //请求ip和主机名
    String address


    static constraints = {
    }
}
