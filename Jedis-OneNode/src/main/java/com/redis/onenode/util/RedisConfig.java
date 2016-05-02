package com.redis.onenode.util;

/**
 * Redis常量配置类
 * Created by Administrator on 2015/7/19 0019.
 */
public class RedisConfig {

    public static int maxTotal = 20;
    /**
     * 控制一个pool最多有多少个状态为idle(空闲)的jedis实例；
     * */
    public static int maxIdle = 10;

    /**
     * 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
     * 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted。
     */
    public static int maxActive = 10;

    public static int maxWaitMillis = 600000;

    public static int timeout = 60000;

    public static int retryNum = 60000;

}
