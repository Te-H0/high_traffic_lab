package teho.high_traffic_lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teho.high_traffic_lab.dto.OrderDetailResponse;
import teho.high_traffic_lab.kafka.KafkaProducer;
import teho.high_traffic_lab.service.CommonService;
import teho.high_traffic_lab.service.CouponService;
import teho.high_traffic_lab.service.OrderService;
//import teho.high_traffic_lab.dto.UserDto;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final KafkaProducer kafkaProducer;
    private final CouponService couponService;
    private final CommonService commonService;
    private final OrderService orderService;

    @GetMapping("/init")
    public String test(@RequestParam int user, @RequestParam int item, @RequestParam int order) {
        commonService.initProject(user, item, order);
        return "yes~";
    }

    @GetMapping("/kafka")
    public String test2(@RequestParam String value) {
        kafkaProducer.send("topic", value);
        return "성공~";
    }

    @GetMapping("/coupon/enqueue")
    public String couponWaitingQueue(@RequestParam long userId) {
        long rank = couponService.enqueueToWaitingQueue(userId);
        return rank + "번째 순서로 대기 중 입니다~!";
    }

    @GetMapping("/coupon/issue")
    public String issueCoupon(@RequestParam long userId) {
        long remainingCouponCount = couponService.issueCoupon(userId);
        if (remainingCouponCount < 0) {
            return "ㅜㅜㅜ쿠폰 재고 소진!!!" + (100 - remainingCouponCount) + "번째로 신청하셨습니다.";
        }
        return (100 - remainingCouponCount) + "번째로 쿠폰 신청 성공 ~!";
    }

    @GetMapping("/coupon/init")
    public String createCoupon(@RequestParam int size) {
        couponService.initCoupons(size);
        return size + "개 쿠폰 rdbms에 저장";
    }

    @GetMapping("orders/detail")
    public OrderDetailResponse orderDetail(@RequestParam Long orderId) {
        return orderService.getOrderDetailByOrderId(orderId);
    }

}
