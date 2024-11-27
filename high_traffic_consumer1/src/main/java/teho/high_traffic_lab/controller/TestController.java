package teho.high_traffic_lab.controller;

import lombok.RequiredArgsConstructor;
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
}
