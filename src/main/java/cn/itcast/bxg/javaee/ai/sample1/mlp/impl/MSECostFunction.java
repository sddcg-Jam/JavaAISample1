/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.mlp.impl;

import cn.itcast.bxg.javaee.ai.sample1.mlp.CostFunction;

/**
 * 代价函数
 * mean-square error 均方误差，简写MSE
 * 参数估计中均方误差是指参数估计值与参数真值之差平方的期望值，记为MSE。
 */
public final class MSECostFunction implements CostFunction {
    /**
     * 计算估计值和实际值的误差
     * 使用均方差进行
     *
     * @param aElement 输出值
     * @param yElement 实际标签值standard flag
     * @return
     */
    @Override
    public double computeCost(double aElement, double yElement) {
        return Math.pow(aElement - yElement, 2) / 2.0; //两值差的平方除2
    }

    /**
     * 求导数
     *
     * @param aElement the output of the last layer
     * @param yElement standard flag
     * @return
     */
    @Override
    public double computeDerivative(double aElement, double yElement) {
        return aElement - yElement;
    }
}
