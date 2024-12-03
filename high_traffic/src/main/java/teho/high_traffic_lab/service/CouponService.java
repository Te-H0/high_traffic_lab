package teho.high_traffic_lab.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teho.high_traffic_lab.dto.CouponIssueRequest;
import teho.high_traffic_lab.entity.Coupon;
import teho.high_traffic_lab.kafka.KafkaProducer;
import teho.high_traffic_lab.redis.RedisUtil;
import teho.high_traffic_lab.repository.CouponRepository;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CouponService {
    public static final String COUPON_QUEUE_KEY = "COUPON_WAITING_QUEUE";
    public static final int PROCESS_SIZE = 1000;
    private static final long COUPON_COUNT = 1000L;
    private static boolean COUPON_STATE = true;
    private final KafkaProducer kafkaProducer;
    private final RedisUtil redisUtil;
    private final CouponRepository couponRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public long enqueueToWaitingQueue(long userId) {

        long score = Instant.now().toEpochMilli();
        redisUtil.addToSortedSet(COUPON_QUEUE_KEY, String.valueOf(userId), score);

        return redisUtil.getMyRankFromSortedSet(COUPON_QUEUE_KEY, String.valueOf(userId));
    }

    @Scheduled(fixedRate = 1000)
    public void processCouponIssue() {
        Set<String> processUsers = redisUtil.getUsersFromSortedSet(COUPON_QUEUE_KEY, PROCESS_SIZE);
        if (!processUsers.isEmpty()) {
            if (!COUPON_STATE) {
                System.out.println("쿠폰 마감 ㅜㅜㅜㅜㅜ");

            } else {
                CompletableFuture[] futures = processUsers.stream()
                        .map(user -> CompletableFuture.runAsync(() -> {
                            if (!COUPON_STATE) {
                                return;
                            }
                            long leftCoupons = issueCoupon(Long.parseLong(user));
                            if (leftCoupons == -1) {
                                COUPON_STATE = false;
                                System.out.println("매진 ㅜㅜ userId: " + Long.parseLong(user));
                            } else {
                                System.out.println("userId: " + Long.parseLong(user) + ", " + (COUPON_COUNT - leftCoupons) + "번째 당첨~!");
                            }

                        }))
                        .toArray(CompletableFuture[]::new);
                CompletableFuture.allOf(futures).join();
            }
            redisUtil.removeRangeFromSortedSet(COUPON_QUEUE_KEY, 0, processUsers.size() - 1);
            System.out.println("대기열에 남은 사람!!!!!!! = " + redisUtil.getQueueSize(COUPON_QUEUE_KEY));
        }


    }

    public long issueCoupon(long userId) {
        long couponCount = redisUtil.decrementValue("COUPON");
        System.out.println("잔여 쿠폰 수 = " + couponCount);
        if (couponCount >= 0) {
            long couponId = COUPON_COUNT - couponCount;
            CouponIssueRequest request = new CouponIssueRequest(userId, couponId);

            kafkaProducer.send("issue-coupon", convertToJson(request));
        } else {
            return -1;
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
