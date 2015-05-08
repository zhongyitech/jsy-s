package com.jsy.utility

/**
 * �ַ���������
 * Created by lioa on 2015/5/8.
 */
class UtilityString {
    //TODO:û������ʱ,���ݳ��ȷ���ô����?
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
