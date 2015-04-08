package com.jsy.utility

import grails.transaction.Transactional

import java.text.SimpleDateFormat

@Transactional(rollbackFor = Throwable.class)
class CreateNumberService {

    def serviceMethod() {

    }
    public static StringBuffer getFormerNumber(StringBuffer mark){
        Date d = new Date()
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd")
        String da=sdf.format(d)
        mark.append(da.toString())
        return mark
    }
    public static StringBuffer getRandomNumber(StringBuffer mark){
        int endnumber = Math.random()*10000 + Math.random()*1000 + Math.random()*100 + Math.random()*10
        StringBuffer funnocut = mark
        funnocut.append(endnumber.toString())
        return  funnocut
    }
    public static StringBuffer getFullNumber(StringBuffer mark,String id){
        mark.append(id.toString())
        return mark
    }
}
