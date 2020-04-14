package com.wuligao.utils.test.thread;

/**
 * @author wuligao
 * @date 2020/4/13 9:00
 */
public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println("this is a my thread");
        super.run();
    }
}
