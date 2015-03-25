package com.jsy.auth
/**
 * 记录接口url与资源 操作的关系
 */
class OperationsAPI {
    //资源
    String resoureClass
    //url接口
    String url
    //操作类型 craeat delete update read
    String czlx

    static constraints = {
    }
}
