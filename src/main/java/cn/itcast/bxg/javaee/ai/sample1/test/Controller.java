/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * GUI演示
 */
public class Controller implements ActionListener {

    // 构造方法
    public Controller() {
        //添加事件监听
        String btnNames[]= {"标签","数据","训练","识别","清除","连接","断开"};//按钮名
        for(String btnName:btnNames) {
            View.getButton(btnName).addActionListener(this);
        }

        //鼠标按下事件
        View.getPanel().addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                Model.mousePressed(e);
            }
        });
        //鼠标拖动事件，自由画图
        View.getPanel().addMouseMotionListener(new MouseAdapter(){
            public void mouseDragged(MouseEvent e){
                Model.mouseDragged(e);
            }
        });
    }

    // 事件处理方法
    public void actionPerformed(ActionEvent e) {
        // 打开数据文件按钮
        if (e.getSource() == View.getButton("数据")) {
            Model.openFile("数据文件");
        }
        // 打开标签文件按钮
        else if (e.getSource() == View.getButton("标签")) {
            Model.openFile("标签文件");
        }
        // 训练按钮
        else if (e.getSource() == View.getButton("训练")) {
            try {
               //TODO Model.train();// 训练
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        // 测试按钮
        else if (e.getSource() == View.getButton("识别")) {
            Model.test();// 测试
        }
        // 清除画板按钮
        else if (e.getSource() == View.getButton("清除")) {
            Model.clearPanel();// 清除
        }
        // 连接按钮
        else if (e.getSource() == View.getButton("连接")) {
            try {
                Model.connect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        // 断开按钮
        else if (e.getSource() == View.getButton("断开")) {
            Model.disconnect();
        }
    }
}

