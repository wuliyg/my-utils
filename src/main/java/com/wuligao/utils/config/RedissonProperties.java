package com.wuligao.utils.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author wuligao
 * @date 2020/4/15 13:48
 */
@Data
public class RedissonProperties {

    private int timeout = 3000;

    @Value("${redisson.address}")
    private String address;

    @Value("${redisson.password}")
    private String password;

    private int connectionPoolSize = 64;

    private int connectionMinimumIdleSize=10;

    private int slaveConnectionPoolSize = 250;

    private int masterConnectionPoolSize = 250;

    private String[] sentinelAddresses;

    private String masterName;

}
