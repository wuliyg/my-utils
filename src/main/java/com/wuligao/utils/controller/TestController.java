package com.wuligao.utils.controller;

import com.wuligao.utils.lock.DistributedLock;
import com.wuligao.utils.result.ApiWrapMapper;
import com.wuligao.utils.result.ApiWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wuligao
 * @date 2020/4/15 9:01
 */
@RestController("/utils/testController/")
@Slf4j
public class TestController {

    @Autowired
    private DistributedLock distributedLock;

    int count=0;

    @GetMapping("/testDistributeLock")
    public ApiWrapper testDistributeLock() throws InterruptedException {

        int clientCount =1000;
        CountDownLatch countDownLatch = new CountDownLatch(clientCount);
        ExecutorService executorService = Executors.newFixedThreadPool(clientCount);
        long start = System.currentTimeMillis();
        for (int i = 0;i<clientCount;i++){
            executorService.execute(() -> {

                String id = UUID.randomUUID().toString().replaceAll("-","");
                try {
                    distributedLock.lock(id);
                    count++;
                }finally {
                    distributedLock.unlock(id);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        log.info("执行线程数:{},总耗时:{},count数为:{}",clientCount,end-start,count);
        return ApiWrapMapper.success();
    }

}
