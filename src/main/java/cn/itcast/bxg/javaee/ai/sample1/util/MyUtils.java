/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 公用类
 */
public final class MyUtils {

    /**
     * 矩阵相乘
     */
    public static List<List<Double>> matrixMul(List<List<Double>> aMatrix, List<List<Double>> bMatrix) {
        List<List<Double>> result = new ArrayList<>();
        for (int i = 0; i < aMatrix.size(); i++) {
            List<Double> rowResult = new ArrayList<>();
            for (int j = 0; j < aMatrix.get(i).size(); j++) {
                double sum = 0;
                for (int k = 0; k < aMatrix.get(i).size(); k++) {
                    sum += aMatrix.get(i).get(j) * bMatrix.get(k).get(j);
                }
                rowResult.add(sum);
            }
            result.add(rowResult);
        }
        return result;
    }

    /**
     * 假定数组有合适的空间
     *
     * @param rArray 乘积结果数组
     */
    public static void matrixMul(double[][] aArray, double[][] bArray, double[][] rArray) {
        for (int i = 0; i < aArray.length; i++) {
            for (int j = 0; j < bArray[0].length; j++) {
                double sum = 0;
                for (int k = 0; k < bArray.length; k++) {
                    sum += (aArray[i][k] * bArray[k][j]);
                }
                rArray[i][j] = sum;
            }
        }
    }

    /**
     * 矩阵加法
     * 结果放入第一个参数中
     */
    public static void matrixPlus(double[][] aArray, double[][] bArray) {
        for (int i = 0; i < aArray.length; i++) {
            for (int j = 0; j < aArray[0].length; j++) {
                aArray[i][j] += bArray[i][j];
            }
        }
    }

    /**
     * 假定数组有合适的空间
     *
     * @param rArray 返回的结果数组
     */
    public static void matrixPlus(double[][] aArray, double[][] bArray, double[][] rArray) {
        for (int i = 0; i < aArray.length; i++) {
            for (int j = 0; j < aArray[0].length; j++) {
                rArray[i][j] = aArray[i][j] + bArray[i][j];
            }
        }
    }

    /**
     * 矩阵转置，把结果放入第二个参数 weightsT.
     * 假定数组有合适的空间.
     *
     * @param weights  原矩阵
     * @param weightsT 目标矩阵（结果）
     */
    public static void matrixTranspose(double[][] weights, double[][] weightsT) {
        for (int i = 0; i < weightsT.length; i++) {
            for (int j = 0; j < weightsT[0].length; j++) {
                weightsT[i][j] = weights[j][i];
            }
        }
    }

    /**
     * 把一维转换成二维，把结果放入第二个参数arrayTwoDimension
     * [i] --> [i][1]
     * 假定第二个参数有合适的大小.
     *
     * @param array             原矩阵
     * @param arrayTwoDimension 目标矩阵 (result)
     */
    public static void toTwoDimension(double[] array, double[][] arrayTwoDimension) {
        for (int i = 0; i < arrayTwoDimension.length; i++) {
            arrayTwoDimension[i][0] = array[i];
        }
    }

    /**
     * 把一维转换成二维
     * [i] --> [i][1]
     *
     * @param array 原矩阵
     * @return 目标矩阵 (result)
     */
    public static double[][] toTwoDimension(double[] array) {
        double[][] arrayTwoDimension = new double[array.length][1];
        for (int i = 0; i < arrayTwoDimension.length; i++) {
            arrayTwoDimension[i][0] = array[i];
        }
        return arrayTwoDimension;
    }

    /**
     * 把二维矩阵转换成一维矩阵，把结果放入arrayOneDimension.
     * [i][1] --> [i]
     * 假定 arrayOneDimension 有合适的空间
     *
     * @param array             原矩阵
     * @param arrayOneDimension 目标矩阵 (result)
     */
    public static void toOneDimension(double[][] array, double[] arrayOneDimension) {
        for (int i = 0; i < arrayOneDimension.length; i++) {
            arrayOneDimension[i] = array[i][0];
        }
    }

    /**
     * 把字节转换成16进制字符串
     *
     * @param bytes bytes
     * @return 返回16进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 根据标签创建独热（one hot）向量
     *
     * @param label     label表示第label个元素的值为1,其他为0
     * @param dimension 维度或者向量长度
     * @return one hot向量
     */
    public static double[] createOneHot(double label, int dimension) {
        double[] oneHot = new double[dimension];
        oneHot[(int) label] = 1;
        return oneHot;
    }

    /**
     * min-max标准化(Min-max normalization) 也叫（线性函数归一化）
     * 也叫离差标准化，是对原始数据的线性变换，使结果落到[0,1]区间，转换函数如下：
     * （X-Min）/(Max-Min)
     * 如果想要将数据映射到-1,1，则将公式换成：（X-Mean）/(Max-Min)
     *
     * @param data data 结果写入参数中
     */
    public static void minMaxNormalization(double[] data) {
        double max = data[0];
        double min = data[0];
        //获取最大、最小值
        for (int i = 1; i < data.length; i++) {
            if (data[i] > max) {
                max = data[i];
                continue;
            }
            if (data[i] < min) {
                min = data[i];
            }
        }

        //标准化
        if (max != min) {
            for (int i = 0; i < data.length; i++) {
                data[i] = (data[i] - min) / (max - min);
            }
        }
    }

    /**
     * 把一个双精度数组转成证书数组
     *
     * @param dArray double array
     * @return int array
     */
    public static int[] dArrayToiArray(double[] dArray) {
        int[] iArray = new int[dArray.length];
        for (int i = 0; i < dArray.length; i++) {
            iArray[i] = (int) dArray[i];
        }
        return iArray;
    }

    /**
     * 生成灰度图片，格式为jpeg
     *
     * @param pixelValues 按列存放的像素值
     * @param width       宽度
     * @param high        高度
     * @param fileName    图片名
     */
    public static void drawGrayPicture(double[] pixelValues, int width, int high, String fileName) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, high, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < high; j++) {
                int pixel = 255 - (int) pixelValues[i * high + j];
                int value = pixel + (pixel << 8) + (pixel << 16);// r = g = b
                // 时，正好为灰度
                bufferedImage.setRGB(j, i, value);
            }
        }
        ImageIO.write(bufferedImage, "JPEG", new File(fileName));
    }

    /**
     * 字节流转图片对象
     *
     * @param bi
     * @param width
     * @param height
     * @return
     */
    public static double[] conver2Image(Image bi, int width, int height, boolean flag) {
        //构建图片流
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //绘制改变尺寸后的图
        tag.getGraphics().drawImage(bi, 0, 0, width, width, null);
        int w = tag.getWidth();//获取宽
        int h = tag.getHeight();//获取长
        double[] rgbArray = new double[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int rgb = tag.getRGB(j, i);//获取RGB值
                int r = (rgb & 0xff0000) >> 16;  //分割为R\G\B
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);
                int gray = (r + g + b) / 3;//转换为灰度
                if (flag && gray < 50) {
                    gray = 255;
                }
                rgbArray[i * w + j] = (gray >= 255 / 2) ? 0 : (255 - gray);// (255 - gray) < 255 / 2 ? 0 : (255 - gray);//(gray>=255/2)?0:1;
            }
        }
        return rgbArray;
    }

    /**
     * 字节流转图片对象
     *
     * @param bi
     * @param width
     * @param height
     * @return
     */
    public static double[] conver2Image(Image bi, int width, int height) {
        return conver2Image(bi, width, height, false);
    }

    /**
     * 返回一组数组中值最大的下标
     *
     * @param output
     * @return 最大值所在的下标
     */
    public static int getMaxSubIndex(double[] output) {
        int pos = 0;
        double max = output[0];
        for (int j = 1; j < output.length; j++) {
            if (output[j] > max) {
                max = output[j];
                pos = j;
            }
        }
        return pos;
    }

    /**
     * 通过序列化方式存储模型
     *
     * @param fileName 模型存放的文件名
     */
    public static <T> void saveModel(String fileName, T obj) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 恢复模型
     *
     * @param fileName 模型持久化的存放位置 文件名
     *                 <p>
     *                 //@SuppressWarnings("unchecked")
     */
    public static <T> T restoreModel(String fileName) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName));
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用删除alpha值的方式去掉图像的alpha通道
     *
     * @param $image
     * @return
     */
    protected static BufferedImage get24BitImage(BufferedImage $image) {
        int __w = $image.getWidth();
        int __h = $image.getHeight();
        int[] __imgARGB = getRGBs($image.getRGB(0, 0, __w, __h, null, 0, __w));
        BufferedImage __newImg = new BufferedImage(__w, __h, BufferedImage.TYPE_INT_RGB);
        __newImg.setRGB(0, 0, __w, __h, __imgARGB, 0, __w);
        return __newImg;
    }

    /**
     * 使用绘制的方式去掉图像的alpha值
     *
     * @param $image
     * @param $bgColor
     * @return
     */
    protected static BufferedImage get24BitImage(BufferedImage $image, Color $bgColor) {
        int $w = $image.getWidth();
        int $h = $image.getHeight();
        BufferedImage img = new BufferedImage($w, $h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor($bgColor);
        g.fillRect(0, 0, $w, $h);
        g.drawRenderedImage($image, null);
        g.dispose();
        return img;
    }

    /**
     * 将32位色彩转换成24位色彩（丢弃Alpha通道）
     *
     * @param $argb
     * @return
     */
    public static int[] getRGBs(int[] $argb) {
        int[] __rgbs = new int[$argb.length];
        for (int i = 0; i < $argb.length; i++) {
            __rgbs[i] = $argb[i] & 0xFFFFFF;
        }
        return __rgbs;
    }

    /**
     * 转成JPG
     *
     * @param image
     * @param save
     * @throws IOException
     */
    @Deprecated
    public static void toJPG(BufferedImage image, File save) throws IOException {
        FileOutputStream out = new FileOutputStream(save);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        //如果图像是透明的，就丢弃Alpha通道
        if (image.getTransparency() == Transparency.TRANSLUCENT) {
            image = get24BitImage(image);
        }
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);//使用jpeg编码器
        param.setQuality(1, true);//高质量jpg图片输出
        encoder.encode(image, param);
        out.close();
    }

    /**
     * 从BASE64编码格式转成图片的RGB一维数组
     *
     * @param fileStr
     * @return
     * @throws Exception
     */
    public static double[] convert2RGBArrayFromBase64(String fileStr) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes1 = decoder.decodeBuffer(fileStr);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
        BufferedImage myImage = ImageIO.read(bais);//Todo
        //double[] rgbArray = MyUtils.conver2Image(myImage, 28, 28, true);
        //            try {
//                ImageIO.write(myImage, "png", new File("E:\\work\\bxg\\mlp\\web0.png"));
//                MyUtils.drawGrayPicture(rgbArray, 28, 28, "E:\\work\\bxg\\mlp\\web0gray.jpg");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        // create a blank, RGB, same width and height, and a white background
        BufferedImage newBufferedImage = new BufferedImage(myImage.getWidth(),
                myImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        //TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
        newBufferedImage.createGraphics().drawImage(myImage, 0, 0, Color.WHITE, null);
        double[] rgbArray = MyUtils.conver2Image(newBufferedImage, 28, 28);

        // write to jpeg file
            /*            try {
                ImageIO.write(newBufferedImage, "jpg", new File("E:\\work\\bxg\\mlp\\web1.jpg"));
                MyUtils.drawGrayPicture(rgbArray, 28, 28, "E:\\work\\bxg\\mlp\\web1gray.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        return rgbArray;
    }

    /**
     * 将一维数组转换成INDArray
     *
     * @param arrays
     * @return
     */
    public static INDArray convert2INDArray(double[] arrays) {
        double[][] twoDArrays = new double[1][arrays.length];
        twoDArrays[0] = arrays;
        INDArray result = Nd4j.create(twoDArrays);
        return result;
    }
}
