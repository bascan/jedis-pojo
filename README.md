# jedis-pojo
Simple self-loading pojo cache service based on Jedis.

##### Maven dependency:
```xml
<dependency>
	<groupId>io.interact</groupId>
	<artifactId>jedis-pojo</artifactId>
	<version>0.0.1</version>
</dependency>
```

Optional: exclude jackson-databind to avoid version conflicts, which can typically happen when it
is loaded as a transitive dependency by e.g. Dropwizard and Jersey.
```xml
<groupId>com.fasterxml.jackson.core</groupId>
<artifactId>jackson-databind</artifactId>
<version>2.6.3</version>
```

##### Usage:
Here's an example the shows you how to use the self-loading capability of the cache.
You basically pass a Supplier with a lambda to the CacheService, which is invoked when
the instance was not found.
```java
// Get an instance of MyModelClass from the cache located at endpoint 'localhost' when it was found.
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