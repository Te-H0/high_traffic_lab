package teho.high_traffic_lab.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponIssueRequest {
    private Long userId;
    private Long couponId;

    public CouponIssueRequest(Long userId, Long couponId) {
        this.userId = userId;
        this.couponId = couponId;
    }


}