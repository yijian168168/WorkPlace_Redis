package com.redis.onenode.junit;

import com.redis.onenode.util.JedisUtil;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Jedis 单机环境 测试工具类
 *
 * Created by zhangqingrong on 2016/5/1.
 */
public class JedisTest {

    private String ip = "192.168.223.128";

    private String port = "7001";

    private JedisUtil jedisUtil;

    private Jedis jedis;

    @Before
    public void init(){
        jedisUtil = JedisUtil.getInstance();
        jedis = jedisUtil.getJedis(ip, Integer.parseInt(port));
        // 清空数据
        System.out.println("flushDB:" + jedis.flushDB());
    }

    @Test
    public void testMap(){

        //增加 成功时返回 "OK"
        System.out.println(jedis.set("set_test1","1"));

        //查询
        System.out.println("set_test1:" + jedis.get("set_test1"));

        //修改
        // 直接覆盖原来的数据 成功时返回 "OK"
        System.out.println(jedis.set("set_test1","new1"));
        System.out.println("set_test1:" + jedis.get("set_test1"));
        //append到已经有的value之后 返回追加的个数  成功时返回  追加后的字符串长度
        System.out.println(jedis.append("set_test1","_append"));
        System.out.println("set_test1:" + jedis.get("set_test1"));

        //删除 返回删除的个数
        System.out.println(jedis.del("set_test1"));
        System.out.println("set_test1:" + jedis.get("set_test1"));
    }

    @Test
    public void testMap2(){
        /**
         * mset相当于
         * jedis.set("mset_test1","1");
         * jedis.set("mset_test2","2");
         */
        //批量增加
        jedis.mset("mset_test1", "1", "mset_test2", "2");
        System.out.println(jedis.get("mset_test1"));
        System.out.println(jedis.get("mset_test2"));
        //批量获取
        System.out.println(jedis.mget("mset_test1", "mset_test2"));

        //批量删除 返回删除数据的个数
        System.out.println(jedis.del("mset_test1","mset_test2"));
        System.out.println(jedis.get("mset_test1"));
        System.out.println(jedis.get("mset_test2"));
    }

    @Test
    public void testKey() {
        //新加入一个key
        jedis.set("testKey_1", "testKey_1_value");

        // 判断key否存在
        System.out.println("1:" + jedis.exists("testKey_1"));
        System.out.println("2:" + jedis.get("testKey_1"));

        /**清空Redis缓存中的所有数据*/
        System.out.println("3:" + jedis.flushDB());

        // 判断key否存在
        System.out.println("4:" + jedis.exists("testKey_1"));
        System.out.println("5:" + jedis.get("testKey_1"));
    }


    @Test
    public void testList() {
        //添加数据
        jedis.rpush("lists", "testList_1_value1");
        jedis.rpush("lists", "testList_1_value2");
        jedis.rpush("lists", "testList_1_value3");

        // 数组长度
        System.out.println("1:" + jedis.llen("lists"));

        // 获取列表指定下标的值
        System.out.println("2:" + jedis.lindex("lists", 1));

        // 再取出所有数据jedis.lrange是按范围取出，
        // 第一个是key，第二个是起始位置，第三个是结束位置， -1表示取得所有
        List<String> values = jedis.lrange("lists", 0, -1);
        System.out.println("3:" + values);


        // 修改列表中单个值
        jedis.lset("lists", 0, "testList_2_value4");
        System.out.println("4:" + jedis.lrange("lists", 0, -1));

        // 列表出栈
        String lists = jedis.lpop("lists");
        System.out.println("5:" + lists);

        // 删除列表指定下标对应的值，如果对应下边的值跟参数中的不同，则返回 "0",如果相同则返回"1"
        System.out.println("6:" + jedis.lrem("lists", 1, "testList_1_value2"));
        System.out.println("7:" + jedis.lrange("lists", 0, -1));

        // 删除区间以外的数据
        System.out.println("8:" + jedis.ltrim("lists", 0, 0));
        System.out.println("9:" + jedis.lrange("lists", 0, -1));
    }


    @Test
    public void testSet() {

        // 添加数据
        jedis.sadd("sets", "HashSet");
        jedis.sadd("sets", "SortedSet");
        jedis.sadd("sets", "TreeSet");

        // 判断value是否在列表中
        System.out.println("1:" + jedis.sismember("sets", "TreeSet"));

        // 整个列表值
        System.out.println("2:" + jedis.smembers("sets"));

        // 删除指定元素
        System.out.println("3:" + jedis.srem("sets", "SortedSet"));
        System.out.println("4:" + jedis.smembers("sets"));

        // 出栈
        System.out.println("5:" + jedis.spop("sets"));
        System.out.println("6:" + jedis.smembers("sets"));

        //
        jedis.sadd("sets1", "HashSet1");
        jedis.sadd("sets1", "SortedSet1");
        jedis.sadd("sets1", "TreeSet");
        jedis.sadd("sets2", "HashSet2");
        jedis.sadd("sets2", "SortedSet1");
        jedis.sadd("sets2", "TreeSet1");
        // 交集
        System.out.println("7:" + jedis.sinter("sets1", "sets2"));
        // 并集
        System.out.println("8:" + jedis.sunion("sets1", "sets2"));
        // 差集
        System.out.println("9:" + jedis.sdiff("sets1", "sets2"));
    }


    @Test
    public void testSortedSet() {

        jedis.zadd("hackers", 1940, "Alan Kay");
        jedis.zadd("hackers", 1953, "Richard Stallman");
        jedis.zadd("hackers", 1965, "Yukihiro Matsumoto");
        jedis.zadd("hackers", 1916, "Claude Shannon");
        jedis.zadd("hackers", 1969, "Linus Torvalds");
        jedis.zadd("hackers", 1912, "Alan Turing");

        //顺序取出
        Set<String> setValues = jedis.zrange("hackers", 0, -1);
        System.out.println("1:" + setValues);

        //倒序取出
        Set<String> setValues2 = jedis.zrevrange("hackers", 0, -1);
        System.out.println("2:" + setValues2);

        // 清空数据
        System.out.println("flushDB:" + jedis.flushDB());

        // 添加数据
        jedis.zadd("zset", 10.1, "hello");
        jedis.zadd("zset", 10.0, ":");
        jedis.zadd("zset", 9.0, "zset");
        jedis.zadd("zset", 11.0, "zset!");

        // 元素个数
        System.out.println("3:" + jedis.zcard("zset"));

        // 元素下标
        System.out.println("4:" + jedis.zscore("zset", "zset"));

        // 集合子集
        System.out.println("5:" + jedis.zrange("zset", 0, 2));

        // 删除元素
        System.out.println("6:" + jedis.zrem("zset", "zset"));
        System.out.println("7:" + jedis.zcount("zset", 9.5, 10.5));

        // 整个集合值
        System.out.println("8:" + jedis.zrange("zset", 0, -1));
    }


    @Test
    public void testHash() {

        Map<String, String> pairs = new HashMap<String, String>();
        pairs.put("name", "Akshi");
        pairs.put("age", "2");
        pairs.put("sex", "Female");

        //添加Map
        jedis.hmset("kid", pairs);

        // 结果是个泛型的LIST
        List<String> name = jedis.hmget("kid", "name");
        System.out.println("1:" + name);

        // 为key中的域 field 的值加上增量 increment
        System.out.println("2:" + jedis.hincrBy("kid", "age", 20));

        //删除map中的某个键值
        jedis.hdel("kid", "age");
        System.out.println("3:" + jedis.hmget("kid", "age")); // 因为删除了，所以返回的是null

        // 返回key为user的键中存放的值的个数
        System.out.println("4:" + jedis.hlen("kid"));

        // 是否存在key为kid的记录
        System.out.println("5:" + jedis.exists("kid"));

        // 判断某个值是否存在
        System.out.println("6:" + jedis.hexists("kid", "sex"));

        // 返回map对象中的所有key
        System.out.println("7:" + jedis.hkeys("kid"));

        // 返回map对象中的所有value
        System.out.println("8:" + jedis.hvals("kid"));

        //返回Map中指定的key对应的value
        List<String> values = jedis.hmget("kid", new String[]{"name", "age", "sex"});
        System.out.println("9:" + values);
        System.out.println("10:" + jedis.hmget("hashs", "name", "age", "sex"));

        //返回Map中的键值对，json格式,{sex=Female, name=Akshi}
        pairs = jedis.hgetAll("kid");
        System.out.println("11:" + pairs);

        Iterator<String> iter = jedis.hkeys("kid").iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + jedis.hmget("kid", key));
        }
    }


    @Test
    public void testOther() throws InterruptedException {

        jedis.set("testOther_1", "testOther_1_value");
        jedis.set("testOther_2", "testOther_2_value");
        jedis.set("testOther_3", "testOther_3_value");
        jedis.set("testOther_4", "testOther_4_value");

        // keys中传入的可以用通配符

        // 返回当前库中所有的key
        System.out.println(jedis.keys("*"));

        // 返回的
        System.out.println(jedis.keys("*_2"));

        // 删除key为sanmdde的对象 删除成功返回1,删除失败（或者不存在）返回 0
        System.out.println(jedis.del("testOther_3"));
        System.out.println(jedis.keys("*"));

        // 返回给定key的有效时间，如果是-1则表示永远有效
        System.out.println(jedis.ttl("testOther_1"));

        // 通过此方法，可以指定key的存活（有效时间） 时间为秒
        //(final String key, final int seconds, final String value) 会重新赋值
        jedis.setex("testOther_4", 10, "min");
        System.out.println(jedis.get("testOther_4"));
        System.out.println(jedis.ttl("testOther_4"));
        Thread.sleep(5000);// 睡眠5秒后，剩余时间将为<=5
        System.out.println(jedis.ttl("testOther_4"));

        //重命名某个key
        System.out.println(jedis.rename("testOther_2", "testOther_5"));
        System.out.println(jedis.get("testOther_2"));// 因为移除，返回为null
        System.out.println(jedis.get("testOther_5")); // 因为将timekey 重命名为time

        // 所以可以取得值 min
        // jedis 排序
        // 注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的）
        //rpush从尾插入，lpush从头插入
        jedis.del("a");// 先清除数据，再加入数据进行测试
        jedis.rpush("a", "1");
        jedis.lpush("a", "6");
        jedis.rpush("a", "3");
        jedis.lpush("a", "9");
        System.out.println(jedis.lrange("a", 0, -1));// [9, 6, 1, 3]
        System.out.println(jedis.sort("a"));//[1, 3, 6, 9]
        //排序后并不保存
        System.out.println(jedis.lrange("a", 0, -1));// [9, 6, 1, 3]
    }


}
