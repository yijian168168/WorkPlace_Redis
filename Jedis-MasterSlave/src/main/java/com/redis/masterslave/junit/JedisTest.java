package com.redis.masterslave.junit;

import com.redis.masterslave.util.JedisUtil;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Jedis 主备环境 测试工具类
 *
 * Created by zhangqingrong on 2016/5/1.
 */
public class JedisTest {

    private String masterIp = "192.168.223.128";

    private String masterPort = "7101";

    private String slaveIp = "192.168.223.128";

    private String slavePort = "7102";

    private JedisUtil jedisUtil;

    private Jedis masterJedis;

    private Jedis slaveJedis;

    @Before
    public void init(){
        jedisUtil = JedisUtil.getInstance();
        masterJedis = jedisUtil.getJedis(masterIp, Integer.parseInt(masterPort));
        slaveJedis = jedisUtil.getJedis(slaveIp, Integer.parseInt(slavePort));
        masterJedis.flushDB();
    }

    @Test
    public void test(){
        System.out.println(masterJedis.set("test","master"));
        System.out.println(masterJedis.get("test"));
        System.out.println(slaveJedis.get("test"));
        System.out.println(masterJedis.flushDB());
        System.out.println(slaveJedis.get("test"));
    }

    @Test
    public void test2(){
        System.out.println(masterJedis.set("testNew","master"));
        System.out.println(masterJedis.get("testNew"));

        System.out.println(slaveJedis.get("testNew"));
        try {
            System.out.println(masterJedis.flushDB());
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            System.out.println(slaveJedis.set("test2","slave"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
