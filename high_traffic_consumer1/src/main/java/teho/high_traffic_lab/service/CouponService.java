package teho.high_traffic_lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teho.high_traffic_lab.kafka.KafkaProducer;
import teho.high_traffic_lab.redis.RedisUtil;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final KafkaProducer kafkaProducer;
    private final RedisUtil redisUtil;

    public String issueCoupon(Long userId) {
        Long couponCount = redisUtil.decrementValue("COUPON_COUNT");
        System.out.println("잔여 쿠폰 수 = " + couponCount);
        if (couponCount < 0) {
            return "쿠폰 발급이 끝났어요~!";
        }

        kafkaProducer.send("issue-coupon", String.valueOf(userId));
        return "쿠폰 발급이 성공했어요~! 쿠폰함을 확인해주세요.";
    }

}
