/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.data;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static cn.itcast.bxg.javaee.ai.sample1.util.MyUtils.bytesToHex;

/**
 * 读取MNIST数据集
 * 本实例所用数据来源于 http://yann.lecun.com/exdb/mnist/
 * 对四个文件下载解压缩
 */
public class MNISTRead {
    public static final String PATH = "D:\\anaconda3\\data\\MNIST";
    public static final String TRAIN_IMAGES_FILE = PATH
            + "/train-images.idx3-ubyte";
    public static final String TRAIN_LABELS_FILE = PATH
            + "/train-labels.idx1-ubyte";
    public static final String TEST_IMAGES_FILE = PATH
            + "/t10k-images.idx3-ubyte";
    public static final String TEST_LABELS_FILE = PATH
            + "/t10k-labels.idx1-ubyte";

    /**
     * 得到训练或者测试图像
     *
     * @param fileName 训练或者测试图像文件名
     * @return 二维数组，一行代表一个图片
     */
    public static double[][] getImages(String fileName) {
        double[][] x = null;
        try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName))) {
            byte[] bytes = new byte[4];
            bin.read(bytes, 0, 4);
            if (!"00000803".equals(bytesToHex(bytes))) {
                throw new RuntimeException("Please select the correct file!");
            } else {
                bin.read(bytes, 0, 4);
                int number = Integer.parseInt(bytesToHex(bytes), 16);
                bin.read(bytes, 0, 4);
                int xPixel = Integer.parseInt(bytesToHex(bytes), 16);
                bin.read(bytes, 0, 4);
                int yPixel = Integer.parseInt(bytesToHex(bytes), 16);
                x = new double[number][xPixel * yPixel];
                for (int i = 0; i < number; i++) {
                    double[] element = new double[xPixel * yPixel];
                    for (int j = 0; j < xPixel * yPixel; j++) {
                        // element[j] = bin.read();
                        // normalization
                        element[j] = bin.read() / 255.0;
                    }
                    x[i] = element;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return x;
    }

    /**
     * 获得训练集或测试集的标签
     *
     * @param fileName 训练集或者测试集标签的文件名
     * @return 一维数组，每一位代表一个标签
     */
    public static double[] getLabels(String fileName) {
        double[] y = null;
        try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName))) {
            byte[] bytes = new byte[4];
            bin.read(bytes, 0, 4);
            if (!"00000801".equals(bytesToHex(bytes))) {
                throw new RuntimeException("Please select the correct file!");
            } else {
                bin.read(bytes, 0, 4);
                int number = Integer.parseInt(bytesToHex(bytes), 16);
                y = new double[number];
                for (int i = 0; i < number; i++) {
                    y[i] = bin.read();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return y;
    }

    public static void main(String[] args) {
//        double[][] images = getImages(TRAIN_IMAGES_FILE);
//        double[] labels = getLabels(TRAIN_LABELS_FILE);

        double[][] images = getImages(TEST_IMAGES_FILE);
        double[] labels = getLabels(TEST_LABELS_FILE);

        System.out.println();
    }

}
