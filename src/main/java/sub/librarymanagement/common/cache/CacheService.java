package sub.librarymanagement.common.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final RedisPublisher redisPublisher;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic cacheInvalidateTopic = new ChannelTopic("cache-invalidate");

    /**
     * 캐시에서 데이터 조회
     */
    public <T> T getCache(String cacheName, String key, Class<T> type) {
        String cacheKey = cacheName + ":" + key;
        return (T) redisTemplate.opsForValue().get(cacheKey);
    }

    /**
     * 캐시에 데이터 저장 (TTL 설정 가능)
     */
    public void setCache(String cacheName, String key, Object value, Duration ttl) {
        String cacheKey = cacheName + ":" + key;
        redisTemplate.opsForValue().set(cacheKey, value, ttl);
    }

    /**
     * 특정 캐시 무효화
     */
    public void evictCache(String cacheName, String key) {
        redisPublisher.publish(cacheInvalidateTopic, new CacheMessage(cacheName, key));
    }

    /**
     * 전체 캐시 무효화
     */
    public void evictAll(String... cacheNames) {
        for (String cacheName : cacheNames) {
            redisPublisher.publish(cacheInvalidateTopic, new CacheMessage(cacheName, null));
        }
    }
}