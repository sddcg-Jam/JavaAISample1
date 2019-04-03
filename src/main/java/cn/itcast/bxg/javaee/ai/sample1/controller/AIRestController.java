/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.controller;


import cn.itcast.bxg.javaee.ai.sample1.dl4j.DL4JDemo;
import cn.itcast.bxg.javaee.ai.sample1.mlp.NeuralNetworkModel;
import cn.itcast.bxg.javaee.ai.sample1.test.NetTrainAndTest;
import cn.itcast.bxg.javaee.ai.sample1.util.MyUtils;
import cn.itcast.bxg.javaee.ai.sample1.util.ProcessBar;
import cn.itcast.bxg.javaee.ai.sample1.util.Stage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI在线demo的控制器
 */
@RestController
public class AIRestController {
    @RequestMapping("/train")
    public String train() {
        //三层网络，各层节点数为784*300*10 输入特征 784个  隐藏层节点300个 输出层节点10个
        int[] nodeNum = {784, 30, 10};// {784, 300,100, 10};
        //周期被定义为向前和向后传播中所有批次的单次训练迭代。
        int epoch = 6; //30
        //每次批量的样本数
        int batchSize = 60;
        double learningRate = 0.5;
        NetTrainAndTest.train(nodeNum, epoch, batchSize, learningRate);
        return "Train my AI";
    }

    /**
     * 获取模型状态进度条
     *
     * @return
     */
    @RequestMapping("/processBar")
    @ResponseBody
    public Stage processBar() {
        return ProcessBar.stageHolder;
    }

    /**
     * 通过恢复模型进行数字识别
     *
     * @param fileStr
     * @return
     */
    @RequestMapping("/recognition")
    public String recognition(String fileStr) {
        try {
            double[] rgbArray = MyUtils.convert2RGBArrayFromBase64(fileStr);
            //装载训练好的网络
            NeuralNetworkModel neuralNetworkModel = MyUtils.restoreModel("E:\\work\\bxg\\mlp\\my2.ai");
            int result = neuralNetworkModel.getOutput(rgbArray);
            return String.valueOf(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "出错了！！！";
    }

    /**
     * 使用DL4J接口恢复模型进行数字识别
     *
     * @param fileStr
     * @return
     */
    @RequestMapping("/recognitionWithDL4j")
    public String recognitionWithDL4j(String fileStr) {
        try {
            double[] rgbArray = MyUtils.convert2RGBArrayFromBase64(fileStr);
            int result = DL4JDemo.recognition(rgbArray);
            return String.valueOf(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "出错了！！！";
    }

}
