package com.jsy.auth
/**
 * 对应属性
 */
class Property implements Comparable{
    //字段名
    String name
    //字段标题
    String title
    //是否显示，此字段不起作用！！！！，直接从ResourceRole的hasmany property判断
    boolean visible

    static constraints = {
    }
    @Override
    int compareTo(Object o) {
        return id.compareTo(o.id)
    }
}
