package com.redis.cluster.junit;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * redis 集群功能测试
 *
 * a:如果集群任意master挂掉,且当前master没有slave.集群进入fail状态,也可以理解成进群的slot映射[0-16383]不完成时进入fail状态.
 * b:如果进群超过半数以上master挂掉，无论是否有slave集群进入fail状态.
 *
 * Created by zhangqingrong on 2016/5/1.
 */
public class JedisClusterTest {

    private JedisCluster jedisCluster;

    @Before
    public void init(){
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.223.128",7201));
        jedisClusterNodes.add(new HostAndPort("192.168.223.128",7202));
        jedisClusterNodes.add(new HostAndPort("192.168.223.128",7203));
        jedisClusterNodes.add(new HostAndPort("192.168.223.128",7204));
        jedisClusterNodes.add(new HostAndPort("192.168.223.128",7205));
        jedisClusterNodes.add(new HostAndPort("192.168.223.128",7206));

        jedisCluster = new JedisCluster(jedisClusterNodes);
    }

    @Test
    public void test(){
        //当集群中的机器有半数以上没有挂掉，集群运行正常
        //当集群中的master挂掉时，会有部分salve自动转换为master
        System.out.println(jedisCluster.set("clusterTest","cluster"));
        System.out.println(jedisCluster.get("clusterTest"));
    }
}
