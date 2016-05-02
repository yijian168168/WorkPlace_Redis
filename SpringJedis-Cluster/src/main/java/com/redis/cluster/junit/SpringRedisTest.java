package com.redis.cluster.junit;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * SpringJedis 集群环境 测试工具类
 * Created by zhangqingrong on 2016/5/2.
 */
public class SpringRedisTest {

    private JedisCluster jedisCluster;

    @Before
    public void init() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring/spring-context.xml");
        jedisCluster = applicationContext.getBean(JedisCluster.class);
    }


    @Test
    public void test() {
        System.out.println(jedisCluster.set("springJedisCluster", "value"));
        System.out.println(jedisCluster.get("springJedisCluster"));
    }

    @Test
    public void testCluster() {
        Map<String, JedisPool> clusters = jedisCluster.getClusterNodes();

        for (JedisPool jedisPool : clusters.values()) {
            System.out.println(jedisPool.getResource().clusterInfo());
            System.out.println("");
            System.out.println(jedisPool.getResource().clusterNodes());
            System.out.println("");
            break;
        }
    }
}
