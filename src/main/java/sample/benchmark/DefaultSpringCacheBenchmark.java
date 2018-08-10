package sample.benchmark;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.CacheManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import sample.BenchmarkRunner;
import sample.model.AirlineStatistic;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class DefaultSpringCacheBenchmark {
  private CacheManager defaultCacheManager;
  private CacheManager jacksonCacheManager;

  private static final String CACHE_NAME = "airline-statistic";
  private static final String WRITE_CACHE_KEY = "write-cache-key";
  private static final String READ_CACHE_KEY = "read-cache-key";

  private AirlineStatistic[] airlineStatistics;

  @Setup(Level.Trial)
  public void initialize() throws Exception {
    ConfigurableApplicationContext applicationContext = SpringApplication.run(BenchmarkRunner.class);

    Set<String> initialCaches = Collections.singleton(CACHE_NAME);

    RedisConnectionFactory connections = applicationContext.getBean(RedisConnectionFactory.class);

    this.defaultCacheManager = RedisCacheManagerBuilder.fromConnectionFactory(connections)
        .initialCacheNames(initialCaches)
        .build();

    ObjectMapper objectMapper = new ObjectMapper();
    RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
    this.jacksonCacheManager = RedisCacheManagerBuilder.fromConnectionFactory(connections)
        .cacheDefaults(cacheConfiguration)
        .initialCacheNames(initialCaches)
        .build();

    Resource jsonResource = applicationContext.getResource("classpath:airlines.json");
    this.airlineStatistics = objectMapper.readValue(jsonResource.getFile(), AirlineStatistic[].class);

    defaultCacheManager.getCache(CACHE_NAME).put(READ_CACHE_KEY, this.airlineStatistics);
    defaultCacheManager.getCache(CACHE_NAME).evict(WRITE_CACHE_KEY);
  }

  @Benchmark
  public void defaultWriteBenchmark(Blackhole blackhole) throws Exception {
    defaultCacheManager.getCache(CACHE_NAME).put(WRITE_CACHE_KEY, airlineStatistics);
    blackhole.consume(1);
  }

  @Benchmark
  public AirlineStatistic[] defultReadBenchmark() {
    return defaultCacheManager.getCache(CACHE_NAME).get(READ_CACHE_KEY, AirlineStatistic[].class);
  }

  @Benchmark
  public void jacksonWriteBenchmark(Blackhole blackhole) throws Exception {
    jacksonCacheManager.getCache(CACHE_NAME).put(WRITE_CACHE_KEY, airlineStatistics);
    blackhole.consume(1);
  }

  @Benchmark
  public AirlineStatistic[] jacksonReadBenchmark() {
    return jacksonCacheManager.getCache(CACHE_NAME).get(READ_CACHE_KEY, AirlineStatistic[].class);
  }

  @TearDown(Level.Trial)
  public void tearDown() {
    defaultCacheManager.getCache(CACHE_NAME).clear();
  }
}
