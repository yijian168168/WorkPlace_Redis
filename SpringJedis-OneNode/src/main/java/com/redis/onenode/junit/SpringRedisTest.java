package com.redis.onenode.junit;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * SpringJedis 单机环境 测试工具类
 *
 * Created by zhangqingrong on 2016/5/2.
 */
public class SpringRedisTest {

    private StringRedisTemplate redisTemplate;

    @Before
    public void init(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
        redisTemplate = applicationContext.getBean(StringRedisTemplate.class);
    }

    @Test
    public void testSet(){
        System.out.println(redisTemplate.opsForSet().add("springRedisTest","springRedis"));
        System.out.println(redisTemplate.boundSetOps("springRedisTest").pop());
    }

    @Test
    public void testHashMap(){
        redisTemplate.opsForHash().put("hashMap","key1","value1");
        System.out.println(redisTemplate.boundHashOps("hashMap").get("key1"));
    }

    @Test
    public void testList(){
        redisTemplate.opsForList().leftPush("list","listValue");
        System.out.println(redisTemplate.boundListOps("list").leftPop());
    }
}
