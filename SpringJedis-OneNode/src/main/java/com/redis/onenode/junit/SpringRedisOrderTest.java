package com.redis.onenode.junit;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author: ZhangQingrong
 * @Date : 2017/11/6 14:37
 */
public class SpringRedisOrderTest {
    private StringRedisTemplate redisTemplate;

    @Before
    public void init() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
        redisTemplate = applicationContext.getBean(StringRedisTemplate.class);
    }

    @Test
    public void testSet() {
        System.out.println(redisTemplate.opsForSet().add("SpringRedisOrderTest", "springRedis"));
        System.out.println(redisTemplate.boundSetOps("SpringRedisOrderTest").pop());
    }

    @Test
    public void testOrderNum() throws InterruptedException {
        redisTemplate.boundValueOps("orderNo").set("0");
        for (int i = 0; i < 10000; i++) {
            new Thread(){
                @Override
                public void run() {
                    System.out.println("result +++ : " + redisTemplate.boundValueOps("orderNo").increment(1));
                }
            }.start();
        }
        Thread.sleep(5000);
        System.out.println("result === : " + redisTemplate.boundValueOps("orderNo").get());
    }
}
