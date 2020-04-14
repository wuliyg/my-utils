package com.wuligao.utils.test;


import com.wuligao.utils.test.thread.MyCallable;
import com.wuligao.utils.test.thread.MyRunnableImpl;
import com.wuligao.utils.test.thread.MyThread;

import java.util.concurrent.FutureTask;

/**
 * @author wuligao
 * @date 2020/3/20 15:55
 */
public class Test {
    public static void main(String[] args) {
        // extends Thread
        MyThread myThread =new MyThread();
        myThread.start();

        // implements Runnable
        MyRunnableImpl runnable=new MyRunnableImpl();
        Thread thread=new Thread(runnable);
        thread.start();

        // implements callable
        MyCallable callable=new MyCallable();
        FutureTask<Integer> ft=new FutureTask<>(callable);
        Thread thread1=new Thread(ft);
        thread1.start();

    }
}
