package teho.high_traffic_lab.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponIssueRequest {
    private long userId;
    private long couponId;

    public CouponIssueRequest(long userId, long couponId) {
        this.userId = userId;
        this.couponId = couponId;
    }

    // getters and setters
}