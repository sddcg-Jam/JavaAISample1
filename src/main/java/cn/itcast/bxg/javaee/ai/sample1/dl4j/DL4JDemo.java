/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */
package cn.itcast.bxg.javaee.ai.sample1.dl4j;

import cn.itcast.bxg.javaee.ai.sample1.util.MyUtils;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Deep Learning for Java
 * DL4J
 *  Author：Jam Fang
 *  https://www.jianshu.com/u/0977ede560d4
 */
public class DL4JDemo {
    /**
     * 模型存放路径
     */
    public static String fileName = "MyMultiLayerNetwork.zip";
    private static Logger log = LoggerFactory.getLogger(DL4JDemo.class);

    /**
     * 测试
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //train();
    }

    /**
     * 训练一个单层模型
     *
     * @return
     * @throws IOException
     */
    public static MultiLayerNetwork train() throws IOException {
        final int numRows = 28; // 矩阵的行数。
        final int numColumns = 28; // 矩阵的列数。
        int outputNum = 10; // 潜在结果（比如0到9的整数标签）的数量。
        int batchSize = 128; // 每一步抓取的样例数量。
        int rngSeed = 123; // 这个随机数生成器用一个随机种子来确保训练时使用的初始权重维持一致。下文将会说明这一点的重要性。
        int numEpochs = 15; // 一个epoch指将给定数据集全部处理一遍的周期。
        DataSetIterator mnistTrain = new MnistDataSetIterator(batchSize, true, rngSeed);
        DataSetIterator mnistTest = new MnistDataSetIterator(batchSize, false, rngSeed);
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(rngSeed)
                //.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Nesterovs(0.006, 0.9))
                .l2(1e-4)
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(numRows * numColumns) // Number of input datapoints.
                        .nOut(1000) // Number of output datapoints.
                        .activation(Activation.RELU) // Activation function.
                        .weightInit(WeightInit.XAVIER) // Weight initialization.
                        .build())
                .layer(1, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(1000)
                        .nOut(outputNum)
                        .activation(Activation.SOFTMAX)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .build();
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        //设置多少迭代打印一次分数 1 iteration
        model.setListeners(new ScoreIterationListener(100));

        log.info("Train model....");
        for (int i = 0; i < numEpochs; i++) {
            model.fit(mnistTrain);
        }

        log.info("Evaluate model....");
        //测试模型
        Evaluation eval = new Evaluation(outputNum); //create an evaluation object with 10 possible classes
        while (mnistTest.hasNext()) {
            DataSet next = mnistTest.next();
            INDArray output = model.output(next.getFeatures()); //get the networks prediction
            eval.eval(next.getLabels(), output); //check the prediction against the true class
        }

        log.info(eval.stats());
        log.info("****************Example finished********************");

        //保存模型
        File locationToSave = new File(fileName);
        ModelSerializer.writeModel(model, locationToSave, true);
        return model;
    }

    /**
     * 通过还原模型进行识别
     *
     * @param rgbArray
     * @return
     * @throws Exception
     */
    public static int recognition(double[] rgbArray) throws Exception {
        //转换成二维矩阵
        INDArray result = MyUtils.convert2INDArray(rgbArray);
        //恢复模型
        File locationToSave = new File(fileName);
        MultiLayerNetwork restored = ModelSerializer.restoreMultiLayerNetwork(locationToSave);
        //用模型识别
        return MyUtils.getMaxSubIndex(restored.output(result).toDoubleVector());
    }

}
