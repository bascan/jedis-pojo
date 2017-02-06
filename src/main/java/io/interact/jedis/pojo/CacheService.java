package io.interact.jedis.pojo;

import java.util.Map;
import java.util.Set;
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
     * @param ttl
     *            Set the cached TTL to the supplied value or don't set the TTL
     *            when the supplied value is null.
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
     * Add supplied values into the set contained at the key.
     * 
     * @param key
     *            key that contains the set
     * @param values
     *            values to insert in the set
     */
    void addToSet(String key, Set<Object> values);

    /**
     * Convenience method that calls
     * {@link CacheService#get(String, Supplier, Class, Integer)} without a TTL.
     * 
     * @param <T>
     *            The pojo that is loaded or cached.
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
     * @param <T>
     *            The pojo that is loaded or cached.
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
     * Get set values contained at key.
     * 
     * @param key
     * @return
     */

    <T> Set<T> getMembers(String key, Supplier<Set<T>> loader, Class<T> out);

    /**
     * Evict value by the supplied key from cache.
     * 
     * @param key
     *            Key of the cached value to evict.
     */
    void evict(String key);

    /**
     * Execute batch delete and update operations on redis sets.
     * 
     * @param deletes
     *            deletes operations : the keys of the map are the redis keys
     *            and the values are the set members to delete in the set
     *            contained at that key
     * @param inserts
     *            inserts operations : the keys of the map are the redis keys
     *            and the values are the set members to insert in the set
     *            contained at that key
     */
    void bulkSetInsertAndDelete(Map<String, Set<Object>> deletes, Map<String, Set<Object>> inserts);

}