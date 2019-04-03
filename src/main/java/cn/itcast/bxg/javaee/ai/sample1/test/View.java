/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.test;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

/**
 * GUI展示 视图层
 *
 */
public class View extends JFrame {
    private static final long serialVersionUID = 1L;// 序列化

    // 定义组件
    private JPanel filePanel, trainPanel, distinguishPanel, serverPanel;// 容器
    private JTabbedPane tabbedPane;// 选项卡
    private static JButton dataTraBtn, tabTraBtn;// 文件选择按钮
    private static JButton traBtn, dstBtn, cleBtn;// 训练&识别按钮
    private static JButton cneBtn, dscneBtn;// 连接&断开按钮
    private static JPanel myPanel;// 画板
    private static JTextField dataTraTexFid, tabTraTexFid;// 文件选择文本框
    private static JProgressBar processBar;// 进度条
    private static JLabel msgLabel;// 信息显示

    // 构造函数
    public View() {

        // 创建选项卡
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);// 设置选项卡标签的布局方式

        // 训练BP神经网络标签页
        trainPanel = new JPanel();
        trainPanel.setLayout(null);// 绝对布局
        JLabel traLab = new JLabel("训练进度：(当进度条显示训练完成时结束)");
        traLab.setBounds(70, 80, 250, 30);
        processBar = new JProgressBar();// 创建进度条
        processBar.setStringPainted(true);// 设置进度条上的字符串显示，false则不能显示
        processBar.setBackground(Color.black);// 设置颜色
        processBar.setBounds(20, 150, 350, 30);
        traBtn = new JButton("开始训练");
        traBtn.setBounds(140, 240, 100, 30);
        trainPanel.add(traLab);
        trainPanel.add(processBar);
        trainPanel.add(traBtn);

        // 手写数字识别标签页
        distinguishPanel = new JPanel();
        distinguishPanel.setLayout(new BorderLayout());
        dstBtn = new JButton("开始识别");
        myPanel = new JPanel();
        cleBtn = new JButton("清除画板");


        JPanel tempPanel=new JPanel();
        tempPanel.setLayout(null);
        tempPanel.add(myPanel);
        myPanel.setBounds(200,250,56,56);//其中200，250表示组件所在的坐标,100,150表示组件的长和宽
        myPanel.setPreferredSize(new Dimension(28, 28));//关键代码,设置JPanel的大小
        myPanel.setBorder(BorderFactory.createEtchedBorder());

        distinguishPanel.add(dstBtn, BorderLayout.NORTH);
        distinguishPanel.add(myPanel, BorderLayout.CENTER);
        distinguishPanel.add(cleBtn, BorderLayout.SOUTH);

        // 网络标签
        serverPanel = new JPanel();
        serverPanel.setLayout(null);// 绝对布局
        cneBtn = new JButton("开启网络连接");
        cneBtn.setBounds(20, 120, 350, 30);
        dscneBtn = new JButton("断开网络连接");
        dscneBtn.setBounds(20, 170, 350, 30);
        msgLabel = new JLabel("。。。");
        msgLabel.setBounds(150, 220, 350, 30);
        serverPanel.add(cneBtn);
        serverPanel.add(dscneBtn);
        serverPanel.add(msgLabel);

        // 将容器添加到选项卡
        tabbedPane.addTab("训练BP神经网络", trainPanel);
        tabbedPane.addTab("手写数字识别", distinguishPanel);
        tabbedPane.addTab("网络", serverPanel);

        // 文件容器
        filePanel = new JPanel();
        filePanel.setLayout(new GridLayout(2, 3, 5, 5));
        String Path = System.getProperty("user.dir");
        dataTraTexFid = new JTextField(Path + "\\mnist_train.csv");
        dataTraBtn = new JButton("浏览");
        tabTraTexFid = new JTextField(Path + "\\mnist_train_labels.csv");
        tabTraBtn = new JButton("浏览");
        filePanel.add(new JLabel("训练数据："));
        filePanel.add(dataTraTexFid);
        filePanel.add(dataTraBtn);
        filePanel.add(new JLabel("训练标签："));
        filePanel.add(tabTraTexFid);
        filePanel.add(tabTraBtn);

        // 添加容器到顶层容器
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        getContentPane().add(filePanel, BorderLayout.SOUTH);

        // 设置窗体属性
        this.setTitle("基于BP的手写数字识别程序");
        this.setSize(400, 480);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    // 获取路径
    public static String getPath(String style) {
        if (style == "数据文件") {
            return dataTraTexFid.getText();
        } else {
            return tabTraTexFid.getText();
        }
    }

    // 设置路径
    public static String setPath(String style, String path) {
        if (style == "数据文件") {
            dataTraTexFid.setText(path);
        } else {
            tabTraTexFid.setText(path);
        }
        return null;
    }

    // 设置进度条进度
    public static void setProgressBarValue(int value) {
        processBar.setValue(value);
    }

    // 设置进度条文本
    public static void setProgressBarString(String str) {
        processBar.setString(str);
    }

    // 设置网络标签信息
    public static void setMessage(String msg) {
        msgLabel.setText(msg);
    }

    // 获取按钮
    public static JButton getButton(String btnName) {
        switch (btnName) {
            case "标签":
                return tabTraBtn;
            case "数据":
                return dataTraBtn;
            case "训练":
                return traBtn;
            case "识别":
                return dstBtn;
            case "清除":
                return cleBtn;
            case "连接":
                return cneBtn;
            case "断开":
                return dscneBtn;
            default:
                break;
        }
        return null;
    }

    // 获取画板
    public static JPanel getPanel() {
        return myPanel;
    }
}