package io.interact.jedis.pojo;

import java.util.function.Supplier;

/**
 * A self-loading cache service based on Redis.
 * 
 * @author Bas Cancrinus - interact.io
 */
public interface CacheService {

    /**
     * Put the supplied key-value pair in cache.
     * 
     * @param key
     *            Key to cached value.
     * @param value
     *            Value to cache. * @param ttl Set the cached TTL to the
     *            supplied value or don't set the TTL when the supplied value is
     *            null.
     */
    void put(String key, Object value, Integer ttl);

    /**
     * Convenience method that calls
     * {@link CacheService#put(String, Object, Integer)} without a TTL.
     * 
     * @param key
     *            Key to cached value.
     * @param value
     *            Value to cache. * @param ttl Set the cached TTL to the
     *            supplied value or don't set the TTL when the supplied value is
     *            null.
     */
    void put(String key, Object value);

    /**
     * Convenience method that calls
     * {@link CacheService#get(String, Supplier, Class, Integer)} without a TTL.
     * 
     * @param key
     *            Key used to lookup and save.
     * @param loader
     *            Supplies the value when it is not cached.
     * @param out
     *            Class of the cached or loaded value.
     * @return Loaded or cached value.
     */
    <T> T get(String key, Supplier<T> loader, Class<T> out);

    /**
     * Get cached value by the supplied key. When that value is not found, then
     * get the value from the supplied loader and put it in the cache with the
     * supplied TTL in seconds.
     * 
     * @param key
     *            Key used to lookup and save.
     * @param loader
     *            Supplies the value when it is not cached.
     * @param out
     *            Class of the cached or loaded value.
     * @param ttl
     *            Set the cached TTL to the supplied value or don't set the TTL
     *            when the supplied value is null.
     * @return Loaded or cached value.
     */
    <T> T get(String key, Supplier<T> loader, Class<T> out, Integer ttl);

    /**
     * Evict value by the supplied key from cache.
     * 
     * @param key
     *            Key of the cached value to evict.
     */
    void evict(String key);

}