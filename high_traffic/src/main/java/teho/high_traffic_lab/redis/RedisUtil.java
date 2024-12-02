package teho.high_traffic_lab.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;

    public void addToSortedSet(String key, String value, long score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public void removeRangeFromSortedSet(String key, int start, int end) {
        redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    public Set<String> getUsersFromSortedSet(String key, int count) {
        return redisTemplate.opsForZSet().range(key, 0, count);
    }

    public long getMyRankFromSortedSet(String key, String value) {
        Long rank = redisTemplate.opsForZSet().rank(key, value);
        if (rank == null) {
            return 0L;
        }
        return rank;
    }

    public long getQueueSize(String key) {
        Long size = redisTemplate.opsForZSet().size(key);
        if (size == null) {
            return 0L;
        }
        return size;
    }

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Long decrementValue(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.decrement(key, 1);// key에 해당하는 값 감소
    }

    public String getValue(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

}
