package teho.high_traffic_lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teho.high_traffic_lab.kafka.KafkaProducer;
import teho.high_traffic_lab.service.CouponService;
import teho.high_traffic_lab.service.UserService;
//import teho.high_traffic_lab.dto.UserDto;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final UserService userService;
    private final KafkaProducer kafkaProducer;
    private final CouponService couponService;

    @GetMapping("/init")
    public String test(@RequestParam int user, @RequestParam int item) {
        userService.createUser(user);
        userService.createItem(item);
        return "yes~";
    }

    @GetMapping("/init2")
    public String test2(@RequestParam int user, @RequestParam int item) {
        userService.createUser2(user);
        userService.createItem2(item);
        return "yes~";
    }

    @GetMapping("/kafka")
    public String test2(@RequestParam String value) {
        kafkaProducer.send("topic", value);
        return "성공~";
    }

    @GetMapping("/coupon/issue")
    public String issueCoupon(@RequestParam Long userId) {
        long remainingCouponCount = couponService.issueCoupon(userId);
        if (remainingCouponCount < 0) {
            return "ㅜㅜㅜ쿠폰 재고 소진!!!" + (100 - remainingCouponCount) + "번째로 신청하셨습니다.";
        }
        return (100 - remainingCouponCount) + "번째로 쿠폰 신청 성공 ~!";
    }

    @GetMapping("/coupon/create")
    public String createCoupon(@RequestParam int size) {
        couponService.createCoupons(size);
        return size + "개 쿠폰 rdbms에 저장";
    }

    @DeleteMapping("/coupon")
    public String deleteCoupon() {
        couponService.deleteCoupons();
        return "쿠폰 전체 삭제 성공";
    }
}
