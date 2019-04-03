/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.test;

import cn.itcast.bxg.javaee.ai.sample1.util.FileLog;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public final class FileLogTest {

    private static final String FILE_NAME = "log/cn.itcast.bxg.javaee.ai.sample1.mlp/file_log_test.txt";
    private static FileLog fileLog;

    @BeforeClass
    public static void before() throws IOException {
        fileLog = new FileLog(FILE_NAME);
        fileLog.open();
    }

    @AfterClass
    public static void after() {
        fileLog.close();
    }

    @Test
    public void testPrintStr() {
        fileLog.print("hello_world!!!");
    }

    @Test
    public void testPrintArray() {
        double[] dArray = {1., 2., 3.};
        fileLog.print(dArray, "dArray");
    }

    @Test
    public void testPrint2Array() {
        double[][] dArrays = {
                {1., 2., 3.},
                {1., 2., 3.},
                {1., 2., 3.}
        };
        fileLog.print(dArrays, "dArrays");
    }

}
