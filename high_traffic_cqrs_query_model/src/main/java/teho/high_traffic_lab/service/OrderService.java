package teho.high_traffic_lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teho.high_traffic_lab.document.OrderInfo;
import teho.high_traffic_lab.repository.OrderInfoRepository;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderInfoRepository orderInfoRepository;

    public OrderInfo getOrderInfoByOrderId(String orderId) {
        return orderInfoRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
