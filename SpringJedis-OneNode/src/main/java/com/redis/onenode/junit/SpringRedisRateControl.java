package com.redis.onenode.junit;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ZhangQingrong
 * @Date : 2017/11/6 14:59
 */
public class SpringRedisRateControl {

    private static StringRedisTemplate redisTemplate;

    @Before
    public void init() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
        redisTemplate = applicationContext.getBean(StringRedisTemplate.class);
    }

    /**
     * 速率控制
     */
    @Test
    public void rateControl() throws InterruptedException {
//        redisTemplate.delete("orderNo");
        String script = " local times = redis.call('incr','orderNo') " +
                " if times == 1 then " +
                " redis.call('expire','orderNo',1) ;" +
                " end" +
                " if times > 9 then " +
                " return 0 ;" +
                " end " +
                " return 1 ;";
        for (int i = 1; i < 201; i++) {
            Long testLua = (Long) executeScript(script);
            System.out.println("++++ " + i + " : " + testLua);
            Thread.sleep(100);
        }
    }

    /**
     * 执行脚本
     */
    public static Object executeScript(final String script) {
        Object execute = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                Jedis jedis = (Jedis) connection.getNativeConnection();
                Object eval = jedis.eval(script);
                return eval;
            }
        });
        return execute;
    }
}
