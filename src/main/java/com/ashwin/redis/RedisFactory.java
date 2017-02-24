package com.ashwin.redis;

import com.ashwin.config.RedisConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisFactory {
    private final JedisPool jedisPool;

    public RedisFactory(RedisConfig redisConfig) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8);
        jedisPool = new JedisPool(poolConfig, redisConfig.host, redisConfig.port);
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }
}
