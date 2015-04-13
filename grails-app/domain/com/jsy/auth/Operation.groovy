package com.jsy.auth
/**
 * 对应操作
 */
class Operation implements Comparable{

    //操作
    String cz
    //名称
    String name
    //标题
    String title
    //对应权限
    boolean visible

    static constraints = {
    }
    @Override
    int compareTo(Object o) {
        return id.compareTo(o.id)
    }
}
