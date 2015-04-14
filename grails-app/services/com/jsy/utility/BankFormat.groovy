package com.jsy.utility

/**
 * Created by lioa on 2015/4/14.
 */
class BankFormat  {
    private  String stringBuilder
    Map<String, Closure<String>> map


    BankFormat(Map<String,Closure<String>> closureMap) {
        this.map=closureMap
    }

    def GetValue(String text,def arg){
        this.stringBuilder=text
        map.each {
            this.stringBuilder=this.stringBuilder.replace(it.getKey(),it.getValue().call(arg))
        }
    }
}
