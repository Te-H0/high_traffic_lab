package teho.high_traffic_lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teho.high_traffic_lab.dto.ItemInfoDto;
import teho.high_traffic_lab.dto.OrderDetailResponse;
import teho.high_traffic_lab.dto.OrderInfoDto;
import teho.high_traffic_lab.repository.ItemRepository;
import teho.high_traffic_lab.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    public OrderDetailResponse getOrderDetailByOrderId(Long orderId) {
        OrderInfoDto orderInfoDto = orderRepository.getOrderInfoListByOrderId(orderId);
        List<ItemInfoDto> itemInfoDtoList = itemRepository.getItemInfoListByOrderId(orderId);

        List<OrderDetailResponse.OrderItemDetail> list = itemInfoDtoList.stream().map(OrderDetailResponse.OrderItemDetail::new)
                .toList();

        return new OrderDetailResponse(orderInfoDto, list);
    }
}
