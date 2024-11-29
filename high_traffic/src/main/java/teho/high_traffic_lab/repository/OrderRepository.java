package teho.high_traffic_lab.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import teho.high_traffic_lab.dto.OrderInfoDto;
import teho.high_traffic_lab.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT new teho.high_traffic_lab.dto.OrderInfoDto(u.name, u.level, o.orderDate, p.status, p.paymentDate) " +
            "FROM Order o " +
            "JOIN User u ON o.userId = u.id " +
            "JOIN Payment p ON o.id = p.orderId " +
            "WHERE o.id = :orderId")
    OrderInfoDto getOrderInfoListByOrderId(@Param("orderId") long orderId);
}
