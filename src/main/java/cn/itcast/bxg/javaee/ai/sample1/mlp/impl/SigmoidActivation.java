/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.mlp.impl;

import cn.itcast.bxg.javaee.ai.sample1.mlp.ActivationFunction;

import static java.lang.Math.E;
import static java.lang.Math.pow;

/**
 * sigmoid 函数
 * sigmoid函数是机器学习中的一个比较常用的函数，与之类似的还有softplus和softmax等函数，是一个在生物学中常见的S型函数，也称为S型生长曲线。
 * Sigmoid变换产生一个值为0到1之间更平滑的范围。
 * S(x)=1/(1+e^(-x))
 * 其导数可用自身表示
 * S`(x)=e^(-x)/((1+e^(-x))^2)=S(x)(1-S(x))
 *
 * 参见：https://baike.baidu.com/item/Sigmoid%E5%87%BD%E6%95%B0/7981407?fr=aladdin
 */
public final class SigmoidActivation implements ActivationFunction {

    @Override
    public double activate(double zElement) {
        return 1.0 / (1.0 + pow(E, -zElement));
    }

    @Override
    public double activate(double zElement, double[] z) {
        throw new UnsupportedOperationException();
    }

    /**
     * * 其导数可用自身表示
     *  * S`(x)=e^(-x)/((1+e^(-x))^2)=S(x)(1-S(x))
     * @param zElement element of mid value
     * @return
     */
    @Override
    public double computeDerivative(double zElement) {
        return activate(zElement) * (1 - activate(zElement));
    }

    @Override
    public double computeDerivative(double zElement, double[] z) {
        throw new UnsupportedOperationException();
    }

}
