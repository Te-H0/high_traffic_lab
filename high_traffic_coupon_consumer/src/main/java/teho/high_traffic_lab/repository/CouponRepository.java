package teho.high_traffic_lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teho.high_traffic_lab.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
