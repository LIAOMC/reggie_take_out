import com.lmc.reggie.ReggieApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = ReggieApplication.class)
@RunWith(SpringRunner.class)
public class Test2 {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 操作String类型数据
     */
    @Test
    public void testString(){
        redisTemplate.opsForValue().set("city123","sichuan");
        String city = (String) redisTemplate.opsForValue().get("city123");
        System.out.println(city);
        //设定一个键值，10秒后失效
        redisTemplate.opsForValue().set("key1","value1",10l, TimeUnit.SECONDS);
        //如果不存在键值对再设置，存在就不设置
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("city1234", "nanjing");
        System.out.println(aBoolean);
    }
    /**
     * 操作Hash类型的数据
     */
    @Test
    public void testHash(){
        HashOperations hashOperations = redisTemplate.opsForHash();
        //存值
        hashOperations.put("002","name","JACK");
        hashOperations.put("002","age","20");
        hashOperations.put("002","address","beijing");
        //取值
        String name = (String) hashOperations.get("002", "name");
        System.out.println(name);
        //获得所有002的字段
        Set keys = hashOperations.keys("002");
        keys.forEach(System.out::println);
        //获得所有002的值
        Set value = hashOperations.keys("002");
        value.forEach(System.out::println);
    }
    /**
     * 操作List类型的数据
     */
    @Test
    public void testList(){
        ListOperations listOperations = redisTemplate.opsForList();
        //存值
        listOperations.leftPush("myList","a");
        listOperations.leftPushAll("myList","b","c","d");
        //取值
        List<String> myList = listOperations.range("myList", 0, -1);
        myList.forEach(System.out::println);
        //获得昌都size
        int size = listOperations.size("myList").intValue();
        for (int i = 0; i < size; i++) {
            //出队列
            String element = (String) listOperations.rightPop("myList");
            System.out.println(element);
        }
    }
    /**
     * 操作Set类型的数据
     */
    @Test
    public void testSet(){
        SetOperations setOperations = redisTemplate.opsForSet();
        //存值
        setOperations.add("mySet", "a", "b", "c","a");
        //取值
        Set<String> mySet = setOperations.members("mySet");
        mySet.forEach(System.out::println);
        //删除成员
        setOperations.remove("mySet","a","b");
        //取值
        mySet = setOperations.members("mySet");
        mySet.forEach(System.out::println);
    }
    /**
     * ZSet类型的数据
     */
    @Test
    public void testZSet(){
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        //存值
        zSetOperations.add("myZSet","a",10.0);
        zSetOperations.add("myZSet","b",11.0);
        zSetOperations.add("myZSet","c",12.0);
        zSetOperations.add("myZSet","a",13.0);
        //取值
        Set<String> myZSet = zSetOperations.range("myZSet", 0, -1);
        myZSet.forEach(System.out::println);
        //修改
        zSetOperations.incrementScore("myZSet","b",20.0);
        //取值
        myZSet = zSetOperations.range("myZSet", 0, -1);
        myZSet.forEach(System.out::println);
        //删除成员
        zSetOperations.remove("myZSet","a","b");
        //取值
        myZSet = zSetOperations.range("myZSet", 0, -1);
        myZSet.forEach(System.out::println);
    }
    /**
     * 通用操作
     */
    @Test
    public void testCommon(){
        //获取Redis中的所有key
        Set<String> keys = redisTemplate.keys("*");
        keys.forEach(System.out::println);
        //判断某个key是否存在
        Boolean aBoolean = redisTemplate.hasKey("itcast");
        System.out.println(aBoolean);
        //删除置顶的key
        Boolean myZSet = redisTemplate.delete("myZSet");
        //获取指定key对应的value的数据类型
        DataType dataType = redisTemplate.type("myset");
        System.out.println(dataType.name());

    }
}
