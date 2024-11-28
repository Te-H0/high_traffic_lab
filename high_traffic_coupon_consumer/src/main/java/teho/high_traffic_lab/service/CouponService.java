package teho.high_traffic_lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teho.high_traffic_lab.entity.Coupon;
import teho.high_traffic_lab.redis.RedisUtil;
import teho.high_traffic_lab.repository.CouponRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final RedisUtil redisUtil;
    private final CouponRepository couponRepository;

    @Transactional
    public void setCouponUserId(Long userId, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new RuntimeException("Id와 맞는 쿠폰이 없습니다."));
        if (coupon.getUserId() != null) {
            throw new RuntimeException("이미 발급된 쿠폰 입니다!!!");
        }
        coupon.setUserId(userId);
        coupon.setIssuedDate(LocalDateTime.now());
    }

}
