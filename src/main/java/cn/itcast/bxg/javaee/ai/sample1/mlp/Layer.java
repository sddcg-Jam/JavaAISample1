/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.mlp;

import cn.itcast.bxg.javaee.ai.sample1.mlp.impl.SoftmaxActivation;
import cn.itcast.bxg.javaee.ai.sample1.util.MyUtils;

import java.io.Serializable;
import java.util.Random;

/**
 * 层的概念， 人工神经网络是由它搭建的
 * 就是神经网络的层数
 */
public class Layer implements Serializable {

    private int currNodeNum;//当前层节点数
    private int previousNodeNum;//前置层节点数

    private double[][] weights; // 权重 [i][j]
    // i :: the number of current layer (currNodeNum)
    // j :: the number of previous layer (previousNodeNum)
    private double[][] biases;  // 偏置 [i][1]

    private double[][] midValue;   // 层中各个节点未激活时得到的中间值，第一维度为本层节点数量，第二维度为1. mid value (inactivated)
    // [i][1]
    private double[][] outputValue;   // 层中各个节点最终输出值（激活后），表示某一层某个节点的输出 第一维度为本层节点数量，第二维度为1.output
    // [i][1]

    private ActivationFunction activationFun; // 激活函数 activation function

    /**
     * 构造函数
     *
     * @param currNodeNum
     * @param previousNodeNum
     * @param aFunction       激活函数
     */
    public Layer(int currNodeNum, int previousNodeNum, ActivationFunction aFunction) {
        this.currNodeNum = currNodeNum;
        this.previousNodeNum = previousNodeNum;

        this.weights = new double[currNodeNum][previousNodeNum];//两层之间的全连接 权重
        this.biases = new double[currNodeNum][1];//每个当前节点的偏置

        // 随机初始化权重
        Random random = new Random();
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = Math.random() * (random.nextInt(2) == 0 ? -1 : 1);
            }
        }//随机初始化偏置
        for (int i = 0; i < biases.length; i++) {
            for (int j = 0; j < biases[j].length; j++) {
                biases[i][j] = Math.random() * (random.nextInt(2) == 0 ? -1 : 1);
            }
        }

        this.midValue = new double[currNodeNum][1];//中间值初始化
        this.outputValue = new double[currNodeNum][1];//输出值初始化
        this.activationFun = aFunction;
    }

    /**
     * 计算各层的输出 各个节点未激活时得到的中间值
     *
     * @param x 训练集数据
     * @return
     */
    public double[][] computerMidValue(double[][] x) {
        MyUtils.matrixMul(weights, x, midValue);//矩阵相乘，计算每一个节点对应的中间值输出(激活前)
        MyUtils.matrixPlus(midValue, biases);//矩阵加法 每一个节点对应的中间值输出+对应的偏置
        return midValue;
    }

    /**
     * 层中各个节点最终输出值（激活后）
     *
     * @return 当前层中各个节点的最终输出值（激活后）
     */
    public double[][] computerOutputValue() {
        // suppose midValue is correct
        double[] zOneDimension = new double[midValue.length];
        MyUtils.toOneDimension(midValue, zOneDimension);//转换成一维
        //z的长度为当前层的节点数
        for (int i = 0; i < midValue.length; i++) {//遍历每个节点的中间值
            for (int j = 0; j < midValue[0].length; j++) {//第二维度为1，对中间值进行激活
                if (activationFun instanceof SoftmaxActivation) {
                    outputValue[i][j] = activationFun.activate(midValue[i][j], zOneDimension);//softMax激活
                } else {
                    outputValue[i][j] = activationFun.activate(midValue[i][j]);//其他激活函数，如Sigmoid函数
                }
            }
        }
        return outputValue;
    }

    /**
     * 当前节点数
     *
     * @return
     */
    public int getCurrNodeNum() {
        return currNodeNum;
    }

    /**
     * 上层节点数
     *
     * @return
     */
    public int getPreviousNodeNum() {
        return previousNodeNum;
    }

    /**
     * 权重
     *
     * @return
     */
    public double[][] getWeights() {
        return weights;
    }

    /**
     * 偏置
     *
     * @return
     */
    public double[][] getBiases() {
        return biases;
    }

    /**
     * 中间值
     * 层中各个节点未激活时得到的中间值，第一维度为本层节点数量，第二维度为1.
     *
     * @return
     */
    public double[][] getMidValue() {
        return midValue;
    }

    /**
     * 输出值
     * 层中各个节点最终输出值（激活后），第一维度为本层节点数量，第二维度为1.
     *
     * @return
     */
    public double[][] getOutputValue() {
        return outputValue;
    }

    /**
     * 激活函数
     *
     * @return
     */
    public ActivationFunction getActivationFun() {
        return activationFun;
    }

    /**
     * 设置激活函数
     *
     * @param activationFun
     */
    public void setActivationFun(ActivationFunction activationFun) {
        this.activationFun = activationFun;
    }
}
