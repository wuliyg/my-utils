package com.wuligao.utils.test.thread;

import java.util.concurrent.Callable;

/**
 * @author wuligao
 * @date 2020/4/13 9:11
 */
public class MyCallable implements Callable {

    private static Integer COUNT=0;

    @Override
    public Object call() throws Exception {
        System.out.println("this is my callable");
        COUNT++;
        return COUNT;
    }
}
