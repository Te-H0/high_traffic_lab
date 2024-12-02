package teho.high_traffic_lab.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import teho.high_traffic_lab.document.OrderInfo;
import teho.high_traffic_lab.repository.OrderInfoRepository;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final OrderInfoRepository orderInfoRepository;

    @KafkaListener(topics = "insert-order-info-to-mongo", groupId = "insert-order-info-to-mongo-group")
    public void listenInsertOrderInfo(String message) {
        OrderInfo orderInfo = convertFromJson(message);
        orderInfoRepository.save(orderInfo);
    }

    private OrderInfo convertFromJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(jsonString, OrderInfo.class);
        } catch (Exception e) {
            System.out.println("OrderInfo listen convert 에러 = " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
