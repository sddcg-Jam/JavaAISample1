/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.mlp;

import java.io.Serializable;

/**
 * 是个接口，它是 代价函数 的抽象，添加新的代价函数只需要实现它
 */
public interface CostFunction extends Serializable {

    /**
     * 计算某一个元素的代价.
     *
     * @param aElement the output of the last layer
     * @param yElement standard flag
     * @return cost
     */
    double computeCost(double aElement, double yElement);

    /**
     * 计算某一个元素的导数
     * compute derivative about just one element.
     *
     * @param aElement the output of the last layer
     * @param yElement standard flag
     * @return derivative
     */
    double computeDerivative(double aElement, double yElement);

}
