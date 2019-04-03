/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.mlp.impl;

import cn.itcast.bxg.javaee.ai.sample1.mlp.ActivationFunction;

/**
 * 在数学，尤其是概率论和相关领域中，Softmax函数，或称归一化指数函数，是逻辑函数的一种推广。
 * Softmax函数实际上是有限项离散概率分布的梯度对数归一化。它能将一个含任意实数的
 * K维向量“压缩”到另一个K维实向量中，使得每一个元素的范围都在0-1之间，并且所有元素的和为1。
 * <p>
 * 参考：https://baike.baidu.com/item/Softmax%E5%87%BD%E6%95%B0/22772270?fr=aladdin
 */
public class SoftmaxActivation implements ActivationFunction {
    @Override
    public double activate(double zElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double activate(double zElement, double[] z) {
        double sum = 0;
        for (int i = 0; i < z.length; i++) {
            sum += Math.pow(Math.E, z[i]);
        }
        return Math.pow(Math.E, zElement) / sum;
    }


    @Override
    public double computeDerivative(double zElement) {
        throw new UnsupportedOperationException();
    }

    //参见： https://www.jianshu.com/p/c02a1fbffad6
    @Override
    public double computeDerivative(double zElement, double[] z) {
        return activate(zElement, z) * (1 - activate(zElement, z));
    }
}
