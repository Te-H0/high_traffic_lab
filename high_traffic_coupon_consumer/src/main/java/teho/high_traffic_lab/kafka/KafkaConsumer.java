package teho.high_traffic_lab.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import teho.high_traffic_lab.dto.CouponIssueRequest;
import teho.high_traffic_lab.service.CouponService;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CouponService couponService;

    @KafkaListener(topics = "test", groupId = "test_group")
    public void listen(String message) {
        System.out.println("message = " + message);
        System.out.println("소비 성공!");
        System.out.println("message = " + message);
    }

    @KafkaListener(topics = "issue-coupon", groupId = "issue-coupon_group")
    public void listen_coupon(String message) {
        CouponIssueRequest couponIssueRequest = convertFromJson(message);
        Long userId = couponIssueRequest.getUserId();
        Long couponId = couponIssueRequest.getCouponId();
        couponService.setCouponUserId(userId, couponId);

        System.out.println("User: " + userId + "-> couponId: " + couponId + " 발급 완료");
    }

    private CouponIssueRequest convertFromJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, CouponIssueRequest.class);
        } catch (Exception e) {
            System.out.println("쿠폰 issue listen convert 에러 = " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
