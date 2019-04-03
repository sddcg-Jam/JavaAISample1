/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.test;

import cn.itcast.bxg.javaee.ai.sample1.mlp.NeuralNetworkModel;
import cn.itcast.bxg.javaee.ai.sample1.util.MyUtils;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * GUI 展示
 */
public class Model {

    private static final int portNo = 1234;//端口
    private static View view;
    private static int startX, startY, endX, endY;//面板画图参数
    private static ServerSocket server;//服务器端的ServerSocket


    /**
     * 测试BP神经网络
     */

    public static void test() {
        BufferedImage myImage = null;
        try {
            myImage = new Robot().createScreenCapture(new Rectangle(
                    view.getX() + 5, view.getY() + 88,
                    View.getPanel().getSize().width, View.getPanel().getSize().height));
//            //字节流转图片对象
            Image bi = myImage;
//            //构建图片流
            BufferedImage tag = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);
//            //绘制改变尺寸后的图
            tag.getGraphics().drawImage(bi, 0, 0, 28, 28, null);
//            int w = tag.getWidth();//获取宽
//            int h = tag.getHeight();//获取长
//            double[] rgbArray = new double[w * h];
//            for (int i = 0; i < w; i++) {
//                for (int j = 0; j < h; j++) {
//                    int rgb = tag.getRGB(j, i);//获取RGB值
//                    int r = (rgb & 0xff0000) >> 16;  //分割为R\G\B
//                    int g = (rgb & 0xff00) >> 8;
//                    int b = (rgb & 0xff);
//                    int gray = (r + g + b) / 3;//转换为灰度
//                    rgbArray[i * w + j] = (255 - gray) < 255 / 2 ? 0 : (255 - gray);//(gray>=255/2)?0:1;
//                }
//            }
            double[] rgbArray = MyUtils.conver2Image(myImage, 28, 28);

            try {
                ImageIO.write(tag, "JPEG", new File("E:\\work\\bxg\\mlp\\0.jpg"));
                MyUtils.drawGrayPicture(rgbArray, 28, 28, "E:\\work\\bxg\\mlp\\my.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //装载训练好的网络
            NeuralNetworkModel neuralNetworkModel = MyUtils.restoreModel("E:\\work\\bxg\\mlp\\my.ai");
            double[][] x = new double[rgbArray.length][1];
            MyUtils.toTwoDimension(rgbArray, x);
            neuralNetworkModel.feedforward(x);
            int result = neuralNetworkModel.getOutput(rgbArray);
            double[] output = neuralNetworkModel.getOutput();
//                int pos = 0;
//                double max = output[0];
//                //Arrays.sort(output);
//               // StringBuffer sb=new StringBuffer();
//                for (int j = 1; j < output.length; j++) {
//                   // sb.append("结果是"+j+"的概率为"+output[j]+" ");
//                    if (output[j] > max) {
//                        max = output[j];
//                        pos = j;
//                    }
//                }
//            int result = pos;   //测试的结果
            //弹出识别结果
            JOptionPane.showMessageDialog(null, "识别结果是：" + result + "概率" + output[result], "结果", JOptionPane.INFORMATION_MESSAGE);
            //JOptionPane.showMessageDialog(null, "识别结果是：" + result+"概率"+ output[result], "结果", JOptionPane.INFORMATION_MESSAGE);
        } catch (AWTException e1) {
            e1.printStackTrace();
        }
    }

    //打开文件
    public static void openFile(String style) {
        JFileChooser imageFile = new JFileChooser();//创建文件选择对话框
        imageFile.setDialogTitle("请选择需要打开的csv文件...");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("csv文件(*.csv)", "csv");
        imageFile.setFileFilter(filter);
        int i = imageFile.showOpenDialog(view.getContentPane());//显示文件选择对话框
        if (i == JFileChooser.APPROVE_OPTION) {//判断用户单机的是否为“打开”按钮
            File selectedFile = imageFile.getSelectedFile();//获得选中的文件对象
            if (style == "数据文件") {
                View.setPath("数据文件", selectedFile.getPath());//显示文件路径
            } else {
                View.setPath("标签文件", selectedFile.getPath());//显示文件路径
            }
        }
    }

    //清空画板
    public static void clearPanel() {
        Graphics graphics = View.getPanel().getGraphics();
        graphics.clearRect(0, 0, View.getPanel().getSize().width, View.getPanel().getSize().height);//清空myPanel
    }

    //鼠标拖动，自由画图
    public static void mouseDragged(MouseEvent e) {
        Graphics graphics = View.getPanel().getGraphics();
        //获取位置信息
        endX = e.getX();
        endY = e.getY();
        ((Graphics2D) graphics).setColor(Color.black);//设置画笔颜色
        ((Graphics2D) graphics).setStroke(new BasicStroke(25));//设置画笔大小
        ((Graphics2D) graphics).drawLine(startX, startY, endX, endY);//画从上次到当前位置的直线
        //更新位置信息
        startX = endX;
        startY = endY;
    }

    //鼠标按下
    public static void mousePressed(MouseEvent e) {
        //重置startX，startY
        startX = e.getX();
        startY = e.getY();
    }

    //启动服务器
    public static void connect() throws IOException {
        server = new ServerSocket(portNo);
        new Thread() {
            public void run() {
                try {
                    View.setMessage("服务器启动");
                    for (; ; ) {
                        if (!server.isClosed()) {
                            //阻塞,知道有客户端连接
                            Socket socket = server.accept();
                            //通过构造函数，启动线程
                            //TODO  new ThreadServer(socket);//这里将与客户端连接的Socket单独使用一个
                            //线程进行处理
                        }
                    }
                } catch (IOException e) {
                    System.out.println("accept Bong Shakalaka");
                    //e.printStackTrace();
                }
            }
        }.start();
    }

    //关闭服务器
    public static void disconnect() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        View.setMessage("服务器关闭");
    }

    //将接收到的数据处理并返回
    public static String handling(String str) {
        String[] strs = str.split(",");
        double[] grays = new double[784];
        for (int i = 0; i < 784; i++) {
            grays[i] = (double) Integer.parseInt(strs[i]);
        }
        int result = 1;//TODO bp.cn.itcast.bxg.javaee.ai.sample1.test(grays);   //测试的结果
        str = String.valueOf(result);
        str += "\r";
        return str;
    }

    public static void main(String[] args) {
        view = new View();    //视图
        new Controller();    //控制器
    }


}
