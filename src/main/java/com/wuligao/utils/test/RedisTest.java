package com.wuligao.utils.test;

import com.wuligao.utils.lock.DistributedLock;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuligao
 * @date 2020/4/14 18:55
 */
@AllArgsConstructor
@Slf4j
public class RedisTest {

    private static final ThreadPoolExecutor THREADPOOL = new ThreadPoolExecutor(2, 1000, 3,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(3),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    public static int count=0;

    public static void main(String[] args) {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://47.99.218.127:7000");
        config.useSingleServer().setPassword("wuligao");
        final RedissonClient client = Redisson.create(config);

        Long startTime=System.currentTimeMillis();
        int clientCount=1000;
        for(int i=0;i<clientCount;i++){
            THREADPOOL.execute(() -> {
                RLock lock = client.getLock("wuligao_lock");
                lock.lock(50000L, TimeUnit.MILLISECONDS);
                log.info("获取到锁了,开始执行操作");
                count++;
                log.info("count is :"+count);
                lock.unlock();
            });
        }

        Long endTime=System.currentTimeMillis();

        log.info("执行线程数:{},总耗时:{},count数为:{}",clientCount,endTime-startTime,count);
    }

}
