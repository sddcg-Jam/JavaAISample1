/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.util;

public class ProcessBar {
    public static Stage stageHolder=new Stage("读取MNIST测试数据", 3, 4);;

    public static void initProcess() {
        //第一阶段 读取测试数据
        Stage stage1 = new Stage("读取MNIST测试数据", 3, 4);
        Stage stage2 = new Stage("训练模型", 3, 3);
        Stage stage3 = new Stage("测试模型", 3, 3);
    }
}
