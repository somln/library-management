package sub.librarymanagement.common.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class RedisSubscriber implements MessageListener {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RedisSubscriber(final RedisTemplate<String, Object> redisTemplate, RedisMessageListenerContainer redisMessageListener) {
        this.redisTemplate = redisTemplate;
        ChannelTopic topic = new ChannelTopic("cache-invalidate");
        redisMessageListener.addMessageListener(this, topic);
    }

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        try {
            // 메시지 파싱
            CacheMessage cacheMessage = objectMapper.readValue(message.getBody(), CacheMessage.class);

            if (cacheMessage.getCacheName() == null || cacheMessage.getCacheName().isEmpty()) {
                // 캐시 이름이 없으면 로그 남기고 종료
                log.info("Invalid cache message: {}", cacheMessage);
                return;
            }

            if (cacheMessage.getKey() == null) {
                // 특정 캐시 이름에 해당하는 모든 데이터 삭제
                Set<String> keys = redisTemplate.keys(cacheMessage.getCacheName() + ":*");
                if (keys != null && !keys.isEmpty()) {
                    redisTemplate.delete(keys);
                    log.info("Deleted cache keys: {}", keys);
                }
            } else {
                // 특정 키 삭제
                String cacheKey = cacheMessage.getCacheName() + ":" + cacheMessage.getKey();
                redisTemplate.delete(cacheKey);
                log.info("Deleted cache key: {}", cacheKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}