package teho.high_traffic_lab.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;

    public Long incrementValue(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.increment(key, 1);// key에 해당하는 값 증가
    }

    public Long decrementValue(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.decrement(key, 1);// key에 해당하는 값 감소
    }

//    public long decreaseValueWithLock(String key) {
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        return valueOperations.decrement(key, -1);
//    }


    public String getValue(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public String getRecentDataFromList(String key) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        return listOperations.index(key, 0);
    }

    public void trimAndPushLeft(String key, long count, String value) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        listOperations.trim(key, 0, count - 2);
        listOperations.leftPush(key, value);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public void setExpiringValue(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void setListValue(String key, List<String> items) {
        redisTemplate.opsForList().rightPushAll(key, items);
    }

    public List<String> getAllListValue(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
