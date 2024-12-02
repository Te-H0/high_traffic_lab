package teho.high_traffic_lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teho.high_traffic_lab.document.OrderInfo;
import teho.high_traffic_lab.service.OrderService;


@RestController
@RequiredArgsConstructor
public class TestController {
    private final OrderService orderService;

    @GetMapping("orders/detail")
    public OrderInfo orderDetail(@RequestParam Long orderId) {
        return orderService.getOrderInfoByOrderId(String.valueOf(orderId));
    }
}
