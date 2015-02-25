package com.jsy.system

class TypeConfig {
    //配置类型
    int type
    //键
    String mapName
    //值
    int mapValue
    //描述
    String description


    static constraints = {
        description nullable: true
    }
}
