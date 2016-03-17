package io.interact.jedis.pojo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * A self-loading cache implementation based on Redis.
 * 
 * @author Bas Cancrinus - interact.io
 */
public class CacheServiceImpl implements CacheService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Map<String, CacheServiceImpl> INSTANCE_MAP = new HashMap<>();

    private final JedisPool pool;

    /**
     * Get {@link CacheService} instance for the supplied Redis endpoint. The
     * syntax of the supplied endpoint should be <code>host[:port]</code>.
     * 
     * @param endpoint
     *            The Redis endpoint to be accessed by the returned instance.
     * @return Instance dedicated to the supplied endpoint.
     */
    public static CacheService getInstance(String endpoint) {
        synchronized (CacheServiceImpl.class) {
            CacheServiceImpl instance = INSTANCE_MAP.get(endpoint);
            if (instance == null) {
                if (endpoint.contains(":")) {
                    String[] parts = endpoint.split(":");
                    instance = new CacheServiceImpl(parts[0], Integer.parseInt(parts[1]));
                } else {
                    instance = new CacheServiceImpl(endpoint);
                }
                INSTANCE_MAP.put(endpoint, instance);
            }
            return instance;
        }
    }

    private CacheServiceImpl(String host) {
        pool = new JedisPool(new JedisPoolConfig(), host);
    }

    private CacheServiceImpl(String host, int port) {
        pool = new JedisPool(new JedisPoolConfig(), host, port);
    }

    @Override
    public void put(String key, Object value) {
        put(key, value, null);
    }

    @Override
    public void put(String key, Object value, Integer ttl) {
        try (Jedis jedis = pool.getResource()) {
            String json = OBJECT_MAPPER.writeValueAsString(value);
            jedis.set(key, json);
            if (ttl != null) {
                jedis.expire(key, ttl);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T get(String key, Class<T> out) {
        try (Jedis jedis = pool.getResource()) {
            String json = jedis.get(key);
            if (json == null) {
                return null;
            }
            return OBJECT_MAPPER.readValue(json, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T get(String key, Supplier<T> loader, Class<T> out) {
        return get(key, loader, out, null);
    }

    @Override
    public <T> T get(String key, Supplier<T> loader, Class<T> out, Integer ttl) {
        T cached = get(key, out);
        if (cached != null) {
            return cached;
        }

        T loaded = loader.get();
        put(key, loaded, ttl);
        return loaded;
    }

    @Override
    public void evict(String key) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(key);
        }
    }
}
