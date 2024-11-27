package teho.high_traffic_lab.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponIssueRequest {
    private Long userId;
    private Long couponId;

    public CouponIssueRequest(Long userId, Long couponId) {
        this.userId = userId;
        this.couponId = couponId;
    }

    // getters and setters
}