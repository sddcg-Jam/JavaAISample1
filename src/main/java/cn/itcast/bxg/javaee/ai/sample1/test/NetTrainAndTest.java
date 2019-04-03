/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.test;

import cn.itcast.bxg.javaee.ai.sample1.data.MNISTRead;
import cn.itcast.bxg.javaee.ai.sample1.mlp.NeuralNetworkModel;
import cn.itcast.bxg.javaee.ai.sample1.mlp.impl.MSECostFunction;
import cn.itcast.bxg.javaee.ai.sample1.mlp.impl.SigmoidActivation;
import cn.itcast.bxg.javaee.ai.sample1.util.MyUtils;
import cn.itcast.bxg.javaee.ai.sample1.util.ProcessBar;
import cn.itcast.bxg.javaee.ai.sample1.util.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * 对模型使用训练集进行训练，然后使用测试数据集对模型进行测试
 * <p>
 * 对模型进行序列化存储，以备后用
 */
public class NetTrainAndTest {

    public static void main(String[] args) {
        //三层网络，各层节点数为784*300*10 输入特征 784个  隐藏层节点300个 输出层节点10个
        int[] nodeNum = {784, 300,100, 10};
        //周期被定义为向前和向后传播中所有批次的单次训练迭代。
        int epoch = 30;
        //每次批量的样本数
        int batchSize = 60;
        double learningRate=0.5;
        NetTrainAndTest.train(nodeNum, epoch, batchSize,learningRate);
    }

    /**
     * @param nodeNums
     * @param epoch     周期被定义为向前和向后传播中所有批次的单次训练迭代。
     * @param batchSize 每次批量的样本数
     */
    public static void train(int[] nodeNums, int epoch, int batchSize, double learningRate) {
        System.out.println("1.开始读取MNIST数据...");
        //第一阶段 读取测试数据
        Stage stage1 = ProcessBar.stageHolder;
        stage1.setName("读取MNIST测试数据");
        stage1.setCurrentStage(1);
        stage1.setTotalStep(4);
        stage1.setCurrentStep(0);
        double[][] trainImages = MNISTRead.getImages(MNISTRead.TRAIN_IMAGES_FILE);//Mnist数据中的训练集60000*784
        stage1.setCurrentStep(1);
        double[] trainLabels = MNISTRead.getLabels(MNISTRead.TRAIN_LABELS_FILE);//Mnist训练集的标签
        stage1.setCurrentStep(2);

        double[][] testImages = MNISTRead.getImages(MNISTRead.TEST_IMAGES_FILE);//Mnist数据中的测试集10000*784
        stage1.setCurrentStep(3);
        double[] testLabels = MNISTRead.getLabels(MNISTRead.TEST_LABELS_FILE);//Mnist测试集的标签
        stage1.setCurrentStep(4);
        //网络拓扑结构
        List<Integer> netStructureList = new ArrayList<>();
        for (int num : nodeNums) {
            netStructureList.add(num);
        }

        System.out.println("创建模型...");
        NeuralNetworkModel neuralNetworkModel = new NeuralNetworkModel(netStructureList, new MSECostFunction(), new SigmoidActivation(), learningRate);
        double[][] xArray = trainImages;//训练集  60000*784
        double[][] yArray = new double[trainLabels.length][1];//训练集实际标签 60000*10
        for (int i = 0; i < trainLabels.length; i++) {
            yArray[i] = MyUtils.createOneHot(trainLabels[i], 10);//把训练标签转换为独热（one hot）向量，标签是几 数组中对应位置的值为1，其余为0
        }

        System.out.println("2.训练模型...");
        stage1.setName("正在训练模型");
        stage1.setCurrentStage(2);
        stage1.setCurrentStep(0);
        stage1.setTotalStep(epoch);
        //  按照小批量梯度下降算法训练
        neuralNetworkModel.miniBatchSGD(xArray, yArray, batchSize, epoch);
        stage1.setCurrentStep(epoch);
        MyUtils.saveModel("E:\\work\\bxg\\mlp\\my.ai", neuralNetworkModel);
        //测试总样本数量
        int total = testImages.length; //10000;

        System.out.println("3.测试模型...");
        stage1.setName("正在测试模型");
        stage1.setCurrentStage(3);
        stage1.setCurrentStep(0);
        stage1.setTotalStep(total);
        //错误数量
        int errCount = 0;
        for (int i = 0; i < total; i++) {
            stage1.setCurrentStep(i);
            double[][] x = new double[testImages[i].length][1];//测试集
            MyUtils.toTwoDimension(testImages[i], x);
            neuralNetworkModel.feedforward(x);
            double[] output = neuralNetworkModel.getOutput();
            int pos = MyUtils.getMaxSubIndex(output);

            //预期值与实际值不符合，错误数量+1
            if (testLabels[i] != pos) {
                ++errCount;
            }
        }

        stage1.setCurrentStep(total);
        System.out.println("准确率 accuracy: " + ((total - errCount) * 100.0 / total) + " %");
        stage1.setName(((total - errCount) * 100.0 / total) + " %");
    }

}
