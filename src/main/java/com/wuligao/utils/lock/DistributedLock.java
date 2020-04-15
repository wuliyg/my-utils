package com.wuligao.utils.lock;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

/**
 * @author wuligao
 * @date 2020/4/14 18:39
 */
@Service
public class DistributedLock {

    /**
     *  锁键
     */
    private String lockKey = "erp_redis_lock";

    /**
     * 锁过期时间
     */
    protected long internalLockLeaseTime = 30000;

    /**
     * 获取锁的超时时间
     */
    private long timeout = 999999;

    /**
     * 设置参数
     */
    SetParams params = SetParams.setParams().nx().px(internalLockLeaseTime);

    private JedisPool jedisPool;

    /**
     * 加锁
     * @param id
     * @return
     */
    public boolean lock(String id){
        // 获取jedis对象
        Jedis jedis=jedisPool.getResource();
        // 开始时间
        Long start=System.currentTimeMillis();
        try{
            while (true){
                String lock=jedis.set(lockKey,id,params);
                if("OK".equals(lock)){
                    return true;
                }
                // 否则循环等待
                Long to=System.currentTimeMillis()-start;
                if(to>=timeout){
                    return false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }finally {
            jedis.close();
        }
    }

    /**
     * 解锁
     * @param id
     * @return
     */
    public boolean unlock(String id){
        Jedis jedis=jedisPool.getResource();
        String lua="if redis.call('get',KEYS[1]) == ARGV[1] then" +
                "   return redis.call('del',KEYS[1]) " +
                "else" +
                "   return 0 " +
                "end";
        try{
            Object result = jedis.eval(lua, Collections.singletonList(lockKey), Collections.singletonList(id));
            if("1".equals(result.toString())){
                return true;
            }
            return false;
        }finally {
            jedis.close();
        }
    }

}
