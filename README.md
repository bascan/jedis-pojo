# jedis-pojo
Simple self-loading pojo cache service based on Jedis.

##### Usage:
Here's an example the shows you how to use the self-loading capability of the cache.
You basically pass a Supplier with a lambda to the CacheService, which is invoked when
the instance was not found.
```java
// Get an instance of MyModelClass from the cache when it exists.
// Otherwise, get it from the MyModelDao and put it in the cache with a TTL of 5 minutes.
CacheService cache = CacheServiceImpl.getInstance("localhost"));
MyModelClass result = cache.get(key, () -> {
                return MyModelDao.get(key);
            } , MyModelClass.class, 300);
```

Here's an example of the put and evict methods:
```java
CacheService cache = CacheServiceImpl.getInstance("localhost"));
// Put myInstance in the cache for a minute.
cache.put(key, myInstance, 60);

// And evict it
cache.evict(key);
```