package teho.high_traffic_lab.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teho.high_traffic_lab.dto.CouponIssueRequest;
import teho.high_traffic_lab.entity.Coupon;
import teho.high_traffic_lab.kafka.KafkaProducer;
import teho.high_traffic_lab.redis.RedisUtil;
import teho.high_traffic_lab.repository.CouponRepository;

@Service
@RequiredArgsConstructor
public class CouponService {
    private static final Long COUPON_COUNT = 100L;
    private final KafkaProducer kafkaProducer;
    private final RedisUtil redisUtil;
    private final CouponRepository couponRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public long issueCoupon(Long userId) {
        Long couponCount = redisUtil.decrementValue("COUPON");
        System.out.println("잔여 쿠폰 수 = " + couponCount);
        if (couponCount >= 0) {
            Long couponId = COUPON_COUNT - couponCount;
            CouponIssueRequest request = new CouponIssueRequest(userId, couponId);

            kafkaProducer.send("issue-coupon", convertToJson(request));
        } else {
            throw new RuntimeException("ㅜㅜㅜ쿠폰 재고 소진!!!" + (100 - couponCount) + "번째로 신청하셨습니다.");
        }


        return couponCount;
    }

    private String convertToJson(CouponIssueRequest request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            System.out.println("쿠폰 issue send convert 에러 = " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void createCoupons(int size) {
        for (int i = 0; i < size; i++) {
            Coupon coupon = new Coupon();
            couponRepository.save(coupon);
        }
        System.out.println("mariaDb에 init된 coupon 수 = " + size);
    }

    @Transactional
    public void initCoupons(int size) {
        // 데이터베이스 초기화
        entityManager.createNativeQuery("TRUNCATE TABLE coupons").executeUpdate();
        System.out.println("Coupons 테이블이 초기화되었습니다.");

        // 데이터베이스에 쿠폰 init
        createCoupons(size);

        // redis 초기화
        redisUtil.deleteData("COUPON");

        // redis에 쿠폰 size init
        redisUtil.setValue("COUPON", String.valueOf(size));
        String coupon = redisUtil.getValue("COUPON");
        System.out.println("redis에 init된 coupon 수 = " + coupon);

    }
}
