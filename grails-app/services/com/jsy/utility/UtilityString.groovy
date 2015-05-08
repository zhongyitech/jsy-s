package com.jsy.utility

/**
 * 字符串帮助类
 * Created by lioa on 2015/5/8.
 */
class UtilityString {
    //TODO:没有数据时,数据长度访怎么定义?
    static String RequestFormat(String src, int totalLen) {
        if (src == null) return ""
        if (src.length() >= totalLen) return src
        src.padRight(totalLen - src.length(), " ")
    }

    static String RequestFormat(int src, int totalLen) {
        String.format("%0" + totalLen + "d", src).replace(" ", "0");
    }

    static String RequestFormat(Long src, int totalLen) {
        String.format("%0" + totalLen + "d", src).replace(" ", "0");
    }
}
