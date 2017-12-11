package com.redis.cluster.junit;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadFactory;

/**
 * Created by QR on 2016/6/22.
 */
public class JedisClusterTest2 {

    private JedisCluster jedisCluster;

    @Before
    public void init() {
        //CommnsPool2 配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        //最大连接
        poolConfig.setMaxTotal(65535);
        //最大空闲连接
        poolConfig.setMaxIdle(1000);
        //获取连接时的最大等待毫秒数（如果设置为阻塞时BlockWhenExhausted),如果超时就抛出异常，
        //小于零：阻塞不确定的时间，默认-1
        poolConfig.setMaxWaitMillis(10000);
        //逐出扫描的时间间隔（毫秒），如果为负数，则不运行逐出线程，默认-1
        poolConfig.setTimeBetweenEvictionRunsMillis(60000);
        //在获取连接的时候检查有效性，默认为false
        poolConfig.setTestOnBorrow(true);

        //hostAndport
        Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
        //添加任意几个就可以，当第一个连接success,就会通过redis自身查找集群节点的方法，获取到全部节点
        hostAndPorts.add(new HostAndPort("192.168.74.128", 1281));
        //Set<HostAndPort> jedisClusterNode, int timeout, int maxRedirections,final GenericObjectPoolConfig poolConfig
        jedisCluster = new JedisCluster(hostAndPorts, 5000, 1000, poolConfig);
    }

    /**
     * 对Set的操作
     */
    @Test
    public void setTest() throws InterruptedException {

        //插入数据
        String setRet = jedisCluster.set("sKey1", "sValue1");
        System.out.println("setRet : " + setRet);

        //查询数据
        String sValue1 = jedisCluster.get("sKey1");
        System.out.println("sValue1 : " + sValue1);

        //删除键值
        long delRet = jedisCluster.del("sKey1");
        System.out.printf("delRet : " + delRet);

        //验证键是否存在
        boolean existsRet = jedisCluster.exists("sKey1");
        System.out.printf("existsRet : " + existsRet);


        //设置 key 对应的值为 string 类型的 value，并指定此键值对应的有效期，单位：秒。成功返回"OK"
        String setexRet = jedisCluster.setex("key1", 5, "value1");
        System.out.printf("setexRetm : " + setexRet);
        System.out.println("key1 : " + jedisCluster.get("key1"));
        Thread.sleep(70000);
        System.out.println("key1 : " + jedisCluster.get("key1"));

        //设置 key 对应的值为 string 类型的 value。 如果 key 已经存在， 返回 0， nx 是 not exist 的意思。
        long setnxRet2 = jedisCluster.setnx("key2", "value2");
        System.out.println("setnxRet2 : " + setnxRet2);
        long setnxRet1 = jedisCluster.setnx("key1", "value1");
        System.out.println("setnxRet1 : " + setnxRet1);

        //对 key 的值做加加操作,并返回新的值。注意 incr 一个不是 int 的 value 会返回错误，incr 一个不存在的 key，则设置 key 为 1
        jedisCluster.incr("age");
        System.out.println("age : " + jedisCluster.get("age"));
        //同 incr 类似，加指定值 ，key 不存在时候会设置 key，并认为原来的 value 是 0
        jedisCluster.incrBy("age", 5);
        System.out.println("age : " + jedisCluster.get("age"));

    }

    /**
     * 对hash的操作
     */
    @Test
    public void hashTest() {
        //设置 hash field 为指定值，如果 key 不存在，则先创建。
        long hsetRet1 = jedisCluster.hset("hashmap1", "field1", "value1");
        System.out.println("hsetRet1 : " + hsetRet1);

        //测试指定 field 是否存在。
        boolean hexistsRet = jedisCluster.hexists("hashmap1", "field1");
        System.out.println("hexistsRet : " + hexistsRet);

        //获取指定的 hash field。
        String hgetRet1 = jedisCluster.hget("hashmap1", "field1");
        System.out.println("hgetRet1 : " + hgetRet1);

        //删除指定的 hash field。
        long hdelRet = jedisCluster.hdel("", "");
        System.out.println("hdelRet : " + hdelRet);

        //设置 hash field 为指定值，如果 key 不存在，则先创建。如果 field 已经存在，返回 0，nx 是not exist 的意思。
        long hsetnxRet2 = jedisCluster.hsetnx("hashmap2", "field2", "value2");
        System.out.println("hsetnxRet2 : " + hsetnxRet2);
        long hsetnxRet1 = jedisCluster.hsetnx("hashmap1", "field1", "value1");
        System.out.println("hsetnxRet1 : " + hsetnxRet1);


        //返回 hash 的所有 field。
        Set<String> hkeysRet = jedisCluster.hkeys("'");
        System.out.println("hkeysRet : " + hkeysRet);

        //返回 hash 的所有 value。
        List<String> hvalsRet = jedisCluster.hvals("");
        System.out.println("hvalsRet : " + hvalsRet);

        //获取某个 hash 中全部的 filed 及 value。
        Map<String, String> hgetAllRet = jedisCluster.hgetAll("");
        System.out.println("hgetAllRet : " + hgetAllRet);

    }

    /**
     * 对List的操作
     */
    @Test
    public void listTest() {
        //在 key 对应 list 的头部添加字符串元素
        jedisCluster.lpush("list", "value1");

        //在 key 对应 list 的尾部添加字符串元素
        jedisCluster.rpush("list", "value2");

        //在value1之前插入value3
        jedisCluster.linsert("list", BinaryClient.LIST_POSITION.BEFORE, "value1", "value3");

        //设置 list 中指定下标的元素值(下标从 0 开始)
        jedisCluster.lset("list", 3, "value4");

        //返回 key 对应 list 的长度
        jedisCluster.llen("list");

        //返回名称为 key 的 list 中 index 位置的元素
        jedisCluster.lindex("list", 2);

        //从 list 的头部删除元素，并返回删除元素
        jedisCluster.lpop("list");

        //从 list 的尾部删除元素，并返回删除元素
        jedisCluster.rpop("list");

        //从 key 对应 list 中删除 count 个和 value 相同的元素。count>0 时，按从头到尾的顺序删除，
        jedisCluster.lrem("list", -1, "value2");

    }

    /**
     * 集群的操作
     * */
    @Test
    public void testCluster() {
        Map<String, JedisPool> cluster = jedisCluster.getClusterNodes();
        for (JedisPool jedisPool : cluster.values()) {
            Jedis jedis = jedisPool.getResource();

            String clusterinfo = jedis.clusterInfo();
            System.out.println("clusterinfo : " + clusterinfo);

            String clusterNodes = jedis.clusterNodes();
            System.out.println("clusterNodes : " + clusterNodes);

            break;
        }
    }
}
