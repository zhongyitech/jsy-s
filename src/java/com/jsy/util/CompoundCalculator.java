package com.jsy.util;

/**
 * Created by oswaldl on 4/3/15.
 */
import java.io.*;
class CompoundCalculator {
    public static void main(String[] args)
            throws java.io.IOException {
        System.out.println(rfv(2, 0.1, 3));
    }

    /**
     *
     * @param p
     * Present value. the current value of serries of payments
     * @param r
     * The rate of interest pre period
     * @param t
     * Payment period.
     *
     * @return 日复利
     */
    public static double rfv(double p, double r, double t){
        double rate = (1+r);
        return p * Math.pow(rate, t);
    }

    /**
     *
     * @param p 如果是单利，p是欠本金； 如果是复利，p是欠总款（包括各种利息）
     * @param r
     * @param t
     * @return 单利 / 复利
     */
    public static double fv(double p, double r, double t){
        return p * r * t /365;
    }


}
