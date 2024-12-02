package teho.high_traffic_lab.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import teho.high_traffic_lab.document.OrderInfo;

public interface OrderInfoRepository extends MongoRepository<OrderInfo, String> {
}
