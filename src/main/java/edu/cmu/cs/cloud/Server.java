package edu.cmu.cs.cloud;

import redis.clients.jedis.Jedis;

/**
 * A server class that manages resource reservation and checkout using Redis.
 */
public class Server {
    private Jedis jedis;
    private RedisLock lock;

    /**
     * Constructs a Server with the specified Jedis client.
     *
     * @param jedis The Jedis client to use for Redis operations.
     */
    public Server(Jedis jedis) {
        this.jedis = jedis;
        this.lock = new RedisLock(jedis);
    }

    /**
     * Attempts to reserve a resource for a user. This function should
     * return true if and only if the resource was available for reservation.
     * If the function is unable to complete a reservation, it should return
     * immediatly.
     *
     * @param resourceID The ID of the resource to reserve.
     * @param userID The ID of the user attempting to reserve the resource.
     * @return true if the reservation was successful, false otherwise.
     */
    public boolean reserve(String resourceID, Integer userID) {
        // Try to acquire the lock with a TTL of 30 seconds (to prevent deadlock)
        if(jedis.exists(resourceID)) { return false; }
        boolean isLocked = lock.acquireLock(resourceID, 30L);
        if (isLocked) {
            jedis.set(resourceID + ":owner", userID.toString());
            return true;
        }
        return false;
    }

    /**
     * Attempts to checkout (release) a resource for a user.
     *
     * @param resourceID The ID of the resource to checkout.
     * @param userID The ID of the user attempting to checkout the resource.
     * @return true if the checkout was successful, false otherwise.
     */
    public boolean checkout(String resourceID, Integer userID) {
        // TODO: complete this function
        if(jedis.exists(resourceID)) {
            String owner = jedis.get(resourceID + ":owner");
            if(!owner.equals(userID.toString())) {
                return false;
            }
            return lock.releaseLock(resourceID);
        }
        return false;

    }
}
