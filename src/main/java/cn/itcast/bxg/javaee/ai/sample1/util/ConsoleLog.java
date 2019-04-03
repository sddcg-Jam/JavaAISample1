/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.util;

public class ConsoleLog implements Log {

    @Override
    public void print(String logContent) {
        System.out.println(logContent);
    }

    @Override
    public void print(double a, String variableName) {
        System.out.println(variableName + "::==" + String.valueOf(a));
    }

    @Override
    public void print(double[] array, String variableName) {
        if(array.length>11){
            return;
        }
        System.out.print(variableName + "::==[");
        for (double a : array) {
            System.out.print(String.valueOf(a) + ",");
        }
        System.out.println("]");
    }

    @Override
    public void print(double[][] array, String variableName) {
        if(array.length>11||array[0].length>11){
            return;
        }
        System.out.print(variableName + "::==[");
        for (double[] a : array) {
            System.out.print("[");
            for (double b : a) {
                System.out.print(String.valueOf(b) + ",");
            }
            System.out.print("]");
        }
        System.out.println("]");
    }
}
