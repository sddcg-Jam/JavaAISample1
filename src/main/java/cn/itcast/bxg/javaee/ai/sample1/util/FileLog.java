/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class FileLog implements Log {

    private String fileName;
    private BufferedWriter bfWrite;

    public FileLog(String fileName) {
        this.fileName = fileName;
    }

    /**
     * open the file using saving
     *
     * @throws IOException
     */
    public void open() throws IOException {
        bfWrite = new BufferedWriter(new FileWriter(fileName));
    }

    /**
     * close the file using saving
     */
    public void close() {
        if (this.bfWrite != null) {
            try {
                this.bfWrite.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void print(String logContent) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(new Date()).append("]\n").append(logContent).append("\n");
        try {
            bfWrite.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void print(double a, String variableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(new Date()).append("] ");
        sb.append(variableName).append(" --> ").append(a).append("\n");
        try {
            bfWrite.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void print(double[] array, String variableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(new Date()).append("] ");
        sb.append(variableName).append(" -->\n");
        sb.append(Arrays.toString(array));
        sb.append("\n");
        try {
            bfWrite.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void print(double[][] array, String variableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(new Date()).append("] ");
        sb.append(variableName).append(" -->\n");
        sb.append("[");
        for (double[] a : array) {
            sb.append(Arrays.toString(a));
            sb.append("\n");
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append("]");
        sb.append("\n");
        try {
            bfWrite.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
