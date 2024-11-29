package teho.high_traffic_lab.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import teho.high_traffic_lab.dto.ItemInfoDto;
import teho.high_traffic_lab.entity.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT new teho.high_traffic_lab.dto.ItemInfoDto(i.name, c.name, i.price, oi.quantity) " +
            "FROM Order o " +
            "JOIN OrderItem oi ON o.id = oi.orderId " +
            "JOIN Item i ON oi.itemId = i.id " +
            "JOIN Category c ON i.categoryId = c.id " +
            "WHERE o.id = :orderId")
    List<ItemInfoDto> getItemInfoListByOrderId(@Param("orderId") long orderId);
}
