package com.jsy.fundObject

import java.text.SimpleDateFormat

class Finfo {

    int startposition = 0
    int pagesize = 10
    String keyword = ""
    Date startsaledate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse('2000-01-01 00:00:00 ')
    Date startsaledate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse('3000-01-01 00:00:00 ')
    int status = 200

    static constraints = {
    }
}
