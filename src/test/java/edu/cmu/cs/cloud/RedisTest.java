package edu.cmu.cs.cloud;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Usage:
 * mvn test
 *
 * <p>You should pass all the provided test cases before you make any submission.
 *
 * <p>Feel free to add more test cases.
 */
class RedisTest {

    /**
     * Used Java Reflection to access {@link Redis#store} which is private.
     *
     * @throws NoSuchFieldException   if a field with the specified name is
     *                                not found.
     * @throws IllegalAccessException if this {@code Field} object
     *                                is enforcing Java language access control
     *                                and the underlying field is either inaccessible or final.
     */
    @Test
    void type() throws NoSuchFieldException, IllegalAccessException {
        Redis redisClient = new Redis();

        // use reflection to access the private field
        Field field = redisClient.getClass().getDeclaredField("store");
        field.setAccessible(true);
        HashMap<String, Object> store = new HashMap<>();
        store.put("unknown", new Object());
        field.set(redisClient, store);

        assertEquals("OK", redisClient.set("mykey", "cloud"));
        assertEquals("string", redisClient.type("mykey"));

        redisClient.hset("hash", "myfield", "myvalue");
        assertEquals("hash", redisClient.type("hash"));

        redisClient.rpush("list", "myvalue1", "myvalue2", "myvalue3");
        assertEquals("list", redisClient.type("list"));

        assertEquals("none", redisClient.type("the key does not exist"));
        assertEquals("unknown", redisClient.type("unknown"));

    }

    @Test
    void checkType() {
        Redis redisClient = new Redis();
        assertEquals("OK", redisClient.set("mykey", "cloud"));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> redisClient.checkType("mykey", "HASH"));
    }

    @Test
    void set() {
        Redis redisClient = new Redis();

        assertEquals("OK", redisClient.set("mykey", "cloud"));
        assertEquals("OK", redisClient.set("mykey", "cool"));
        assertEquals("OK", redisClient.set("secondkey", "yes"));
        assertNotEquals("Random", redisClient.set("thirdkey", "no"));
    }

    @Test
    void get() {
        Redis redisClient = new Redis();

        assertNull(redisClient.get("mykey"));

        redisClient.set("mykey", "cloud");
        assertNotNull(redisClient.get("mykey"));
        assertEquals("cloud", redisClient.get("mykey"));

        redisClient.set("mykey", "cool");
        assertNotEquals("cloud", redisClient.get("mykey"));
        assertEquals("cool", redisClient.get("mykey"));
    }

    @Test
    void del() {
        Redis redisClient = new Redis();

        assertEquals(0, redisClient.del("mykey"));

        redisClient.set("mykey1", "cloud");
        redisClient.set("mykey2", "cool");
        redisClient.set("mykey3", "awesome");
        assertEquals(2, redisClient.del("mykey1", "mykey3"));

        assertNotEquals(0, redisClient.del("mykey2"));
    }

    @Test
    void hset() {
        Redis redisClient = new Redis();
        assertEquals(1, redisClient.hset("key1", "name", "cloud"));
        assertEquals(0, redisClient.hset("key1", "name", "computing"));
        assertNotEquals(0, redisClient.hset("key2", "sub", "data"));
        assertNotEquals(1, redisClient.hset("key2", "sub", "hci"));
    }

    @Test
    void hget() {
        Redis redisClient = new Redis();
        redisClient.hset("key1", "name", "cloud");
        redisClient.hset("key1", "name", "computing");
        assertEquals("computing", redisClient.hget("key1", "name"));
        assertNull(redisClient.hget("key3", "name"));
        assertNull(redisClient.hget("key1", "address"));
        assertNotNull(redisClient.hget("key1", "name"));
        assertNotEquals("cloud", redisClient.hget("key1", "name"));
    }

    @Test
    void hgetall() {
        Redis redisClient = new Redis();
        redisClient.hset("key1", "name", "computing");
        redisClient.hset("key1", "address", "XYZ");
        List<String> expected = new LinkedList<>();
        expected.add("address");
        expected.add("XYZ");
        expected.add("name");
        expected.add("computing");
        assertEquals(expected, redisClient.hgetall("key1"));
        assertEquals(new ArrayList<>(), redisClient.hgetall("key2"));
        assertNotNull(redisClient.hgetall("key2"));
    }

    @Test
    void llen() {
        Redis redisClient = new Redis();
        redisClient.rpush("key1", "v1","v2","v3");
        assertEquals(0, redisClient.llen("randomly"));
        assertEquals(3, redisClient.llen("key1"));
        assertNotEquals(6, redisClient.llen("key1"));
    }

    @Test
    void rpush() {
        Redis redisClient = new Redis();
        assertEquals(3, redisClient.rpush("key1", "v1","v2","v3"));
        assertEquals(6, redisClient.rpush("key1", "v4","v5","v6"));
        assertNotEquals(0, redisClient.rpush("key2", "v4","v5","v6"));
    }

    @Test
    void rpop() {
        Redis redisClient = new Redis();
        assertNull(redisClient.rpop("key1"));
        redisClient.rpush("key1", "v1","v2","v3");
        assertEquals("v3", redisClient.rpop("key1"));
        assertNotEquals("v1", redisClient.rpop("key1"));
    }
}