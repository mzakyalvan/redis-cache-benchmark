package sample.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CacheServiceImpl.class);

  @Autowired
  StringRedisTemplate redisTemplate;

  @Autowired
  ObjectMapper objectMapper;

  @Override
  public <T> T findCacheByKey(String key, Class<T> clazz) {
    T readValue = null;
    try {
      String content = this.redisTemplate.opsForValue().get(key);
      if (content != null) {
        readValue = this.objectMapper.readValue(content, clazz);
      }
    } catch (Exception e) {
      LOGGER.warn("#failed-to-getCachedValue for key :{}, class:{}, error: {}", key, clazz,
          e.getMessage(), e);
    }
    return readValue;
  }

  @Override
  public Boolean createCache(String key, Object value, long expirySeconds) {
    Boolean success = true;

    try {

      this.redisTemplate.opsForValue().set(key, this.objectMapper.writeValueAsString(value));
      if (expirySeconds > 0) {
        this.redisTemplate.expire(key, expirySeconds, TimeUnit.SECONDS);
      }
    } catch (Exception e) {
      LOGGER.error("CacheServiceImpl-createCache stackTrace = {}",
          e);
      success = false;
    }

    return success;
  }

  @Override
  public Boolean deleteCache(String key) {
    Boolean success = true;

    try {
      this.redisTemplate.delete(key);
    } catch (Exception e) {
      LOGGER.error("CacheServiceImpl-deleteCache stackTrace = {}",
          e);
      success = false;
    }

    return success;
  }

  @Override
  public Boolean deleteCacheByPattern(String pattern) {
    Boolean success = true;

    try {
      Set<String> stringSet = getKeys(pattern);
      stringSet.forEach(keys -> redisTemplate.delete(keys));
    } catch (Exception e) {
      LOGGER.error("CacheServiceImpl-deleteCacheByPattern stackTrace = {}", e);
      success = false;
    }

    return success;
  }

  private Set<String> getKeys(String pattern) {
    return redisTemplate.keys(StringUtils.defaultIfEmpty(pattern, CacheConstant.PATTERN_ALL));
  }


}
