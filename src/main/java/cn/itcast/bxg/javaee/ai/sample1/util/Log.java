/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.util;

/**
 * 日志记录
 */
public interface Log {

    /**
     * output the log
     *
     * @param logContent the content of the log
     */
    void print(String logContent);

    /**
     * output the log
     *
     * @param a            a value
     * @param variableName variable's name
     */
    void print(double a, String variableName);

    /**
     * output the log
     *
     * @param array        the log's content
     * @param variableName variable's name
     */
    void print(double[] array, String variableName);

    /**
     * output the log
     *
     * @param array        the log's content
     * @param variableName variable's name
     */
    void print(double[][] array, String variableName);

}
