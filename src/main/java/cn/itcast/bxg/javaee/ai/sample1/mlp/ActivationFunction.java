/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.mlp;

import java.io.Serializable;

/**
 * 接口，它是 激活函数 的抽象，尝试其他的激活函数只需要实现它
 */
public interface ActivationFunction extends Serializable {

    /**
     * 获取单个节点的输出
     * @param zElement element of mid value
     * @return activated output
     */
    double activate(double zElement);

    double activate(double zElement, double[] z);

    /**
     * 求导数
     * @param zElement element of mid value
     * @return 返回导数
     */
    double computeDerivative(double zElement);
    /**
     * 求偏导数
     * @param zElement element of mid value
     * @return 返回导数
     */
    double computeDerivative(double zElement, double[] z);
}
