package sample.service;

public interface CacheService {

  <T> T findCacheByKey(String key, Class<T> clazz);

  Boolean createCache(String key, Object value, long expirySeconds);

  Boolean deleteCache(String key);

  Boolean deleteCacheByPattern(String pattern);

}
