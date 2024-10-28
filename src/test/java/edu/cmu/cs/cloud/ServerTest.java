package edu.cmu.cs.cloud;

import org.junit.jupiter.api.Test;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.BeforeAll;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Usage:
 * mvn test
 *
 * <p>You should pass all the provided test cases before you make any submission.
 *
 * <p>Feel free to add more test cases.
 */
class ServerTest {

    private static final String CONFIGURATION_FILE = "configuration.yaml";
    private static String globalPath = Paths.get("src", "main", "resources", CONFIGURATION_FILE).toString();
    private static JedisPool jedisPool;

    /**
     * Sets up the test environment before running any tests.
     * Initializes the JedisPool with configuration from a YAML file.
     *
     * @throws IOException if there's an error reading the configuration file
     */
    @BeforeAll
    static void setUp() throws IOException {
        GenericObjectPoolConfig<Jedis> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(50);

        Config config = new Config(Paths.get(globalPath).toFile().getAbsolutePath());
        
        jedisPool = new JedisPool(poolConfig, new HostAndPort(config.getHostName(), 6380), 
            DefaultJedisClientConfig.builder()
            .password(config.getKey())
            .ssl(true)
            .build());
    }

    @Test
    void reserveEmptyResource() {
        Jedis jedis = jedisPool.getResource();
        Server server = new Server(jedis);
        jedis.flushAll();
        assertTrue(server.reserve("resource1", 1));
    }

    @Test
    void reserveOccupiedResource() {
        Jedis jedis = jedisPool.getResource();
        Server server = new Server(jedis);
        jedis.flushAll();
        // user1 reserves the resource
        assertTrue(server.reserve("resource1", 1));
        // user2 tries to reserve same resource
        assertFalse(server.reserve("resource1", 2));
    }

    @Test
    void checkoutOccupiedAndOwnedResource() {
        Jedis jedis = jedisPool.getResource();
        Server server = new Server(jedis);
        jedis.flushAll();
        // user1 reserves resource
        assertTrue(server.reserve("resource1", 1));
        // User1 checks out resource
        assertTrue(server.checkout("resource1", 1));
    }

    @Test
    void checkoutEmptyResource() {
        Jedis jedis = jedisPool.getResource();
        Server server = new Server(jedis);
        jedis.flushAll();
        // user checkout a resource that has not been reserved
        assertFalse(server.checkout("resource1", 1));
    }

    @Test
    void checkoutUnownedResource() {
        Jedis jedis = jedisPool.getResource();
        Server server = new Server(jedis);
        jedis.flushAll();
        // user1 reserves the resource
        assertTrue(server.reserve("resource1", 1));
        // user 2 tries to checkout the resource
        assertFalse(server.checkout("resource1", 2));
    }

    @Test
    void reserveAndCheckout() {
        Jedis jedis = jedisPool.getResource();
        Server server = new Server(jedis);
        jedis.flushAll();

        assertTrue(server.reserve("resource1", 1));
        assertTrue(server.checkout("resource1", 1));
        assertTrue(server.reserve("resource1", 1));
    }

    @Test
    void reserveAndCheckoutMultipleResources() {
        Jedis jedis = jedisPool.getResource();
        Server server = new Server(jedis);
        jedis.flushAll();
        // user1 reserves two resources
        assertTrue(server.reserve("resource1", 1));
        assertTrue(server.reserve("resource2", 1));
        // user1 checks out  first resource
        assertTrue(server.checkout("resource1", 1));
        // user2 reserves first resource after it's checked out
        assertTrue(server.reserve("resource1", 2));
        // user1 checks out second resource
        assertTrue(server.checkout("resource2", 1));
    }
}
