/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.mlp;

import cn.itcast.bxg.javaee.ai.sample1.mlp.impl.SoftmaxActivation;
import cn.itcast.bxg.javaee.ai.sample1.util.ConsoleLog;
import cn.itcast.bxg.javaee.ai.sample1.util.Log;
import cn.itcast.bxg.javaee.ai.sample1.util.MyUtils;
import cn.itcast.bxg.javaee.ai.sample1.util.ProcessBar;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 描述的整个全连接网络，其实它就是 mlp
 * 自己实现一个全连接网络（即，MLP(多层感知机)），并最终能在 MNIST 数据集上取得 95% 左右的准确率；
 * 熟悉 BP(反向传播的过程)；
 * 熟悉 BGD mini-BatchSGD 过程；
 */
public class NeuralNetworkModel implements Serializable {

    // for example
    // nodeNumbers is [3, 4, 5]. The size of layes variable is 2.
    // layes[0] = [3, 4] show :: current layer node's number is 4, previous layer node's number is 3;
    private List<Integer> nodeNumbers; //网络拓扑结构，各层节点数 desc topology of the net
    private Layer[] layers; // layers 网络所含的层

    // about delta array, the size of row equals the size of layers. But, the size of column is uncertain.
    //行等于层数，列等于每层的节点数
    private double[][] deltas;    // 误差 δ (!t's important in the BP!) ★★

    private CostFunction costFun;//代价函数
    private ActivationFunction activationFun;//激活函数
    private double learningRate;//学习率

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    private transient Log log = new ConsoleLog();  //new FileLog("e:\\work\\bxg\\mlp\\log.log");//日志

    /**
     * 网络构造函数
     *
     * @param nodeNumbers 网络拓扑结构
     * @param costFun        代价函数
     * @param activationFun        激活函数
     * @param learningRate     学习率
     */
    public NeuralNetworkModel(List<Integer> nodeNumbers, CostFunction costFun, ActivationFunction activationFun, double learningRate) {
        this.nodeNumbers = nodeNumbers;//网络拓扑结构
        this.costFun = costFun;//代价函数
        this.activationFun = activationFun;//激活函数
        this.learningRate = learningRate;//学习率
        //创建神经网络各层，传入的参数为各层的节点数量。第一个为输入层节点数
        this.layers = new Layer[nodeNumbers.size() - 1];
        for (int i = 0; i < nodeNumbers.size() - 1; i++) {
            //前置节点数量
            int previousNodeNum = nodeNumbers.get(i);
            //当前节点数量
            int currNodeNum = nodeNumbers.get(i + 1);
            //初始化层
            this.layers[i] = new Layer(currNodeNum, previousNodeNum, activationFun);
        }
        this.deltas = new double[this.layers.length][];//各层的误差，一维为每层，二维为层中的各个节点
        for (int i = 0; i < this.deltas.length; i++) {
            this.deltas[i] = new double[this.layers[i].getCurrNodeNum()];
        }
    }

    public NeuralNetworkModel(List<Integer> nodeNumbers, CostFunction costFun, ActivationFunction activationFun) {
        this(nodeNumbers, costFun, activationFun, 1);
    }

    /**
     * 正向传播，默认不输出日志
     * feedforward for one sample, don't output log
     *
     * @param x the sample input 训练集样本
     */
    public void feedforward(double[][] x) {
        feedforward(x, false);
    }

    /**
     * 向前传播得到预测数据
     * feedforward for one sample and can set whether to output log
     *
     * @param x           训练集        x the sample input
     * @param isOutputLog 是否显示日志 true will output log
     */
    public void feedforward(double[][] x, boolean isOutputLog) {
        double[][] tInput = x;//训练集
        if (isOutputLog) {
            log.print(x, "x");
        }
        for (int i = 0; i < layers.length; i++) {//遍历各层进行计算
            Layer layer = layers[i];
            double[][] z = layer.computerMidValue(tInput);//计算各层各个节点未激活时的中间值 第一维度为本层节点数量，第二维度为1.
            double[][] a = layer.computerOutputValue();//计算各层各个节点的输出值（激活后） 第一维度为本层节点数量，第二维度为1.
            tInput = a;//将本层输出作为下层的输入
            if (isOutputLog) {
                log.print(layer.getWeights(), i + "-th layer / " + "W");
                log.print(layer.getBiases(), i + "-th layer / " + "B");
                log.print(z, i + "-th layer / " + "Z");
                log.print(a, i + "-th layer / " + "A");
            }
        }
    }

    /**
     * 计算代价函数，正向传播完成后再计算，默认显示日志
     * note :: please execute it after execute `feedforward(double[][] x)`!
     *
     * @return cost 代价
     */
    public double computeCost(double[][] y) {
        return computeCost(y, false);
    }

    /**
     * 计算代价函数，正向传播完成后再计算
     * compute cost and can set whether to output log.
     * note :: please execute it after execute `feedforward(double[][] x)`!
     *
     * @param y           标签集
     * @param isOutputLog 是否输出日志 true show that output log.
     * @return cost 各个节点平均代价
     */
    public double computeCost(double[][] y, boolean isOutputLog) {
        double cost = 0;
        double[][] a = layers[layers.length - 1].getOutputValue();//获得最后一层的输出
        for (int i = 0; i < a.length; i++) {
            cost += costFun.computeCost(a[i][0], y[i][0]);//计算最后一层输出的误差也就是代价 或损失，每个节点的损失累加
        }
        cost = cost / layers[layers.length - 1].getCurrNodeNum();//代价平分到各个节点
        if (isOutputLog) {
            log.print(cost, "cost");
        }
        return cost;
    }

    /**
     * 反向传播
     * a trip of the back propagation ★★
     * note :: please execute it after execute `feedforward(double[][] x)`!
     *
     * @param x       单个样本     the attrs of one sample
     * @param y       单个样本的标签    the flags of one sample
     * @param deltasB 整个网络的所有偏置deltasB
     * @param deltasW 整个网络的所有权重deltasW
     */
    private void backPropagation(double[][] x, double[][] y, double[][][] deltasB, double[][][] deltasW) {
        //1.先计算最后一层的误差 1st compute deltas[L] (BP1)
        int layerPos = layers.length - 1;
        double[][] a = layers[layerPos].getOutputValue();//最后一层的输出值
        double[][] z = layers[layerPos].getMidValue();//最后一层的中间值
        ActivationFunction aFunction = layers[layerPos].getActivationFun();//激活函数
        double[] zOneDimension = new double[z.length];
        MyUtils.toOneDimension(z, zOneDimension);//中间值一维化
        for (int i = 0; i < deltas[layerPos].length; i++) {//对最后一层的每一个节点计算误差
            if (aFunction instanceof SoftmaxActivation) {
                deltas[layerPos][i] = costFun.computeDerivative(a[i][0], y[i][0]) * aFunction.computeDerivative(z[i][0], zOneDimension);
            } else {
                deltas[layerPos][i] = costFun.computeDerivative(a[i][0], y[i][0]) * aFunction.computeDerivative(z[i][0]);
            }
        }
        --layerPos;//往上移动一层

        // 2.计算其他各层的导数 2st compute deltas[i] (BP2)
        for (; layerPos >= 0; layerPos--) {
            double[][] weights = layers[layerPos + 1].getWeights();//后一层权重
            double[][] weightsT = new double[weights[0].length][weights.length];
            MyUtils.matrixTranspose(weights, weightsT);//矩阵转置
            double[] delta = deltas[layerPos + 1];//后一层导数
            double[][] deltaTwoDimension = new double[delta.length][1];
            MyUtils.toTwoDimension(delta, deltaTwoDimension);//将一维扩展为二维
            double[][] tempDelta = new double[deltas[layerPos].length][1];//临时导数值
            MyUtils.matrixMul(weightsT, deltaTwoDimension, tempDelta);//权重和后一层导数矩阵乘机放入临时导数值中
            z = layers[layerPos].getMidValue();//得到当前层的中间值
            aFunction = layers[layerPos].getActivationFun();//激活函数
            MyUtils.toOneDimension(z, zOneDimension);//中间值降为一维，softmax激活函数会用到，其他激活韩束用不到
            for (int i = 0; i < tempDelta.length; i++) {
                if (aFunction instanceof SoftmaxActivation) {
                    tempDelta[i][0] *= aFunction.computeDerivative(z[i][0], zOneDimension);
                } else {
                    tempDelta[i][0] *= aFunction.computeDerivative(z[i][0]);//下一层导数临时值乘以当前层激活函数导数
                }
            }
            MyUtils.toOneDimension(tempDelta, deltas[layerPos]);//把临时导数值降为一维存入当前层导数
        }

        // 3.更新每个节点的偏置  3rd get deltas b (BP3)
        for (int i = 0; i < layers.length; i++) {
            for (int j = 0; j < layers[i].getCurrNodeNum(); j++) {
                deltasB[i][j][0] += deltas[i][j];
            }
        }

        // 4.更新每个节点的权重 4th get deltas w (BP4)
        for (int i = 0; i < layers.length; i++) {
            // 计算权重compute deltasW
            if (i != 0) {
                a = layers[i - 1].getOutputValue();//上一层输出值
            } else {
                a = x;//顶层时，就是输入的样本
            }
            for (int j = 0; j < deltasW[i].length; j++) {
                for (int k = 0; k < deltasW[i][j].length; k++) {
                    deltasW[i][j][k] += (a[k][0] * deltas[i][j]);
                }
            }
        }
    }

    /**
     * 小批量随机梯度下降法（ Mini-batch stochastic gradient descent，SGD）
     * （1）选择n个训练样本（n<m，m为总训练集样本数）
     * （2）在这n个样本中进行epoch次迭代，每次使用1个样本
     * （3）对epoch次迭代得出的epoch个gradient进行加权平均再并求和，作为这一次mini-batch下降梯度
     * （4）不断在训练集中重复以上步骤，直到收敛。
     *
     * @param xArray    训练集 the attrs of all samples.
     * @param yArray    训练标签集the flags of all samples.
     * @param batchSize 每次批量样本数量the size of batch.
     * @param epoch     周期the number of iterations.
     */
    public void miniBatchSGD(double[][] xArray, double[][] yArray, int batchSize, int epoch) {
        int totalSamplesNum = xArray.length;//训练集数量
        double[][] batchXArray = new double[batchSize][];//按照小批量batchSize分隔 60*784
        double[][] batchYArray = new double[batchSize][];//60*10
        for (int i = 0; i < epoch; i++) {//迭代周期循环
            System.out.format("[开始第%d次迭代，共%d次迭代]" + df.format(new Date()) + " \n", i, epoch);
            ProcessBar.stageHolder.setCurrentStep(i);
            List<Integer> posList = new ArrayList<>();
            for (int j = 0; j < totalSamplesNum; j++) {
                posList.add(j);//生成训练集下标集合
            }
            Collections.shuffle(posList);// 使用默认随机源对下标列表进行洗牌打乱。
            for (int j = 0; j < totalSamplesNum / batchSize; j++) {//拆分训练集
                for (int k = 0; k < batchSize; k++) {//按照batchsize生成每一个小批量的训练集和标签
                    batchXArray[k] = xArray[posList.get(j * batchSize + k)];
                    batchYArray[k] = yArray[posList.get(j * batchSize + k)];
                }
                //System.out.format("[%d-th epoch 第%d个批量]: \n", i,j);
                BGD(batchXArray, batchYArray, 1,false);//对每一个小批量进行 批量梯度下降算法
            }
            // get cost
            // 使用第一个批量集合获取代价 use 1st batch set.
            for (int j = 0; j < batchSize; j++) {
                batchXArray[j] = xArray[posList.get(j)];
                batchYArray[j] = yArray[posList.get(j)];
            }
            double[][] x;
            double[][] y;
            double cost = 0;
            for (int j = 0; j < batchSize; j++) {
                x = MyUtils.toTwoDimension(batchXArray[j]);//new double[batchXArray[j].length][1];
                y = MyUtils.toTwoDimension(yArray[j]);//new double[yArray[j].length][1];
                //MyUtils.toTwoDimension(batchXArray[j], x);
                // MyUtils.toTwoDimension(batchYArray[j], y);
                feedforward(x);
                // compute cost
                cost += computeCost(y);
            }
            System.out.format("[cost of %d-th epoch]: " + (cost / batchSize) +" "+ df.format(new Date())+"\n", i);
        }
    }

    /**
     * 更新偏置或权重
     *
     * @param oldValue 待更新的数据
     * @param rate     学习速率
     * @param total    样本数量
     * @param deltas   导数
     * @return
     */
    private double[][] bpUpdateValue(double[][] oldValue, double rate, int total, double[][] deltas) {
        for (int k = 0; k < oldValue.length; k++) {
            for (int l = 0; l < oldValue[k].length; l++) {
                oldValue[k][l] -= (learningRate / total * deltas[k][l]);
            }
        }
        return oldValue;
    }
    /**
     * 批量梯度下降法 Batch Gradient Descent
     * 在每一次迭代时使用所有样本来进行梯度的更新。
     * every bp using all samples.
     * note: don't output the last cost.
     *
     * @param xArray 训练集the attrs of all samples.
     * @param yArray 标签the flags of all samples.
     * @param epoch  迭代次数number of iterations.
     */
    public void BGD(double[][] xArray, double[][] yArray, int epoch) {
        BGD(xArray, yArray, epoch, false);
    }

    /**
     * 批量梯度下降BGD 进行训练
     * 在每一次迭代时使用所有样本来进行梯度的更新。
     * train by using BGD.
     * note: every bp using all samples.
     *
     * @param xArray      训练集  the attrs of all samples.
     * @param yArray      标签  the flags of all samples.
     * @param epoch       迭代次数 number of iterations.
     * @param isOutputLog 是否输出日志，true显示输出
     */
    public void BGD(double[][] xArray, double[][] yArray, int epoch, boolean isOutputLog) {
        int total = xArray.length;
        for (int i = 1; i <= epoch; i++) {
            double[][][] deltasB = new double[layers.length][][];//偏置，第一维为层数，第二维为层的节点数，第三维为1
            double[][][] deltasW = new double[layers.length][][];//权重，第一维为层数，第二维为当前层的节点数，第三维为前置层的节点数
            for (int j = 0; j < layers.length; j++) {
                deltasB[j] = new double[layers[j].getCurrNodeNum()][1];
                deltasW[j] = new double[layers[j].getCurrNodeNum()][layers[j].getPreviousNodeNum()];
            }
            double[][] x;//把训练集的每一行扩展为二维
            double[][] y;//把训练集标签的每一行扩展为二维
            double cost = 0;//代价函数
            for (int j = 0; j < xArray.length; j++) {//对样本进行遍历，对每一个样本进行前项传播、代价计算、后向传播
                x = MyUtils.toTwoDimension(xArray[j]);//new double[xArray[j].length][1];//初始化二维数组
                y = MyUtils.toTwoDimension(yArray[j]);//new double[yArray[j].length][1];
                //MyUtils.toTwoDimension(xArray[j], x);//每一个样本扩展为二维，第二个维度长度为1
                //MyUtils.toTwoDimension(yArray[j], y);
                feedforward(x,false);//前向传播
                // compute cost
                cost += computeCost(y);//计算代价函数
                backPropagation(x, y, deltasB, deltasW);//后向传播
            }
            // 使用日志输出代价
            if (isOutputLog) {
                System.out.format("[cost before the %d-th epoch]: " + (cost / total) + "\n", i);
                if (log != null) {
                    log.print(String.format("[cost before the %d-th epoch]: " + (cost / total), i));
                }
                // out deltasB deltasW by using log
                if (log != null) {
                    log.print("===================== " + i + "-th epoch =====================");
                    for (int j = deltasB.length - 1; j >= 0; j--) {
                        // about b
                        log.print(deltasB[j], j + "-th layer / deltaB");
                        // about w
                        log.print(deltasW[j], j + "-th layer / deltaW");
                    }
                    log.print("=====================================================");
                }
            }
            // 更新偏置
            for (int j = 0; j < layers.length; j++) {
                double[][] biases = layers[j].getBiases();
               /* for (int k = 0; k < biases.length; k++) {
                    for (int l = 0; l < biases[k].length; l++) {
                        biases[k][l] -= (learningRate / total * deltasB[j][k][l]);
                    }
                }*/
                bpUpdateValue(biases, learningRate, total, deltasB[j]);
            }
            // 更新权重
            for (int j = 0; j < layers.length; j++) {
                double[][] weights = layers[j].getWeights();
               /* for (int k = 0; k < weights.length; k++) {
                    for (int l = 0; l < weights[k].length; l++) {
                        weights[k][l] -= (learningRate / total * deltasW[j][k][l]);
                    }
                }*/
                bpUpdateValue(weights, learningRate, total, deltasW[j]);
            }
        }
    }

    /**
     * 对单个样本进行预测
     *
     * @param rgbArray 单个样本
     * @return 识别结果
     */
    public int getOutput(double[] rgbArray) {
        double[][] x = new double[rgbArray.length][1];
        MyUtils.toTwoDimension(rgbArray, x);
        feedforward(x);
        double[] output = getOutput();
        return MyUtils.getMaxSubIndex(output);
    }

    /**
     * 取得最后一层的输出值，显示日志
     *
     * @return the output of the last layer.
     */
    public double[] getOutput() {
        return getOutput(false);
    }

    /**
     * 取得最后一层的输出值
     *
     * @param isOutputLog 是否显示日志true show that output log.
     * @return the output of the last layer.
     */
    public double[] getOutput(boolean isOutputLog) {
        double[] output = new double[layers[layers.length - 1].getOutputValue().length];
        MyUtils.toOneDimension(layers[layers.length - 1].getOutputValue(), output);
        if (isOutputLog) {
            log.print(output, "output");
        }
        return output;
    }

    /**
     * 获得网络拓扑
     *
     * @return
     */
    public List<Integer> getNodeNumbers() {
        return nodeNumbers;
    }

    /**
     * 获得导数
     *
     * @return
     */
    public double[][] getDeltas() {
        return deltas;
    }

    /**
     * 获得层集合
     *
     * @return
     */
    public Layer[] getLayers() {
        return layers;
    }

    /**
     * 获得代价函数
     *
     * @return
     */
    public CostFunction getCostFun() {
        return costFun;
    }

    /**
     * 获得激活函数
     *
     * @return
     */
    public ActivationFunction getActivationFun() {
        return activationFun;
    }

    /**
     * 获得学习速率
     *
     * @return
     */
    public double getLearningRate() {
        return learningRate;
    }

    /**
     * 设置第i层的激活函数
     *
     * @param i         layer i
     * @param aFunction activation function
     */
    public void setActivationFunction(int i, ActivationFunction aFunction) {
        this.layers[i].setActivationFun(aFunction);
    }

    /**
     * 设置日志log
     *
     * @param log the implement of the log interface.
     * @return this instance
     */
    public void setLog(Log log) {
        this.log = log;
    }

}
