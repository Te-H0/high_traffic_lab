package teho.high_traffic_lab.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "test", groupId = "test_group")
    public void listen(String message) {
        System.out.println("message = " + message);
        System.out.println("소비 성공!");
        System.out.println("message = " + message);
    }
}
