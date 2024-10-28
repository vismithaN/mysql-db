package edu.cmu.cs.cloud;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Objects;


/**
 * A class that implements a distributed lock using Redis.
 */
public class RedisLock {
    private Jedis jedis;

    /**
     * Constructs a RedisLock with the specified Jedis client.
     *
     * @param jedis The Jedis client to use for Redis operations.
     */
    public RedisLock(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * Attempts to acquire a lock with the given key and time-to-live (TTL).
     *
     * @param lockKey The key to use for the lock in Redis.
     * @param ttl The time-to-live for the lock in seconds.
     * @return true if the lock was successfully acquired, false otherwise.
     */
    public boolean acquireLock(String lockKey, Long ttl) {
        // Setting parameters to use NX and EX and
        // to set the lock key with value "locked"
        SetParams setParams = new SetParams().nx().ex(ttl.intValue());
        String result = jedis.set(lockKey, "locked", setParams);
        return "OK".equals(result);
    }


    /**
     * Attempts to release a previously acquired lock.
     * This function should return true only if the lock was
     * acquired and was has been successfully released.
     *
     * @param lockKey The key of the lock to release.
     * @return true if the lock was successfully released, false otherwise.
     */
    public boolean releaseLock(String lockKey) {
        // Check if the lock is currently set to "locked"
        if(Objects.equals(jedis.get(lockKey), "locked")) {
            long result = jedis.del(lockKey);
            return result == 1L;  // Return true if  lock was deleted ,1 means success
        } else {
            return false;
        }
    }
}