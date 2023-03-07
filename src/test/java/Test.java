import redis.clients.jedis.Jedis;

import java.util.Set;


public class Test {
    @org.junit.Test
    public void test1(){
        Jedis jedis=new Jedis("localhost",6379);

        jedis.set("name","jack");

        String name = jedis.get("name");
        System.out.println(name);
        jedis.del("name");//删除

        jedis.hset("myhash","addr","sichuan");
        String hget = jedis.hget("myhash", "addr");
        System.out.println(hget);

        Set<String> keys = jedis.keys("*");//拿到所有Key
        keys.forEach(System.out::println);


        jedis.close();
    }
    public static void main(String[] args) {

    }
}
