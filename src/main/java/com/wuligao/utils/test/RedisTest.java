package com.wuligao.utils.test;

import com.wuligao.utils.lock.DistributedLock;
import redis.clients.jedis.JedisPool;

/**
 * @author wuligao
 * @date 2020/4/14 18:55
 */
public class RedisTest {
    public static void main(String[] args) {

        JedisPool jedisPool=new JedisPool();
        DistributedLock lock=new DistributedLock(jedisPool);
        String lockName = lock.lockWithTimeout("wuligao-lock01", 1000L, 1000L);
        System.out.println("lock is :"+lockName);

        lock.releaseLock("wuligao-lock01",lockName);
        System.out.println("lock is release");
    }
}
