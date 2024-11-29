package teho.high_traffic_lab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    private long id;

    @Column(nullable = false)
    private long orderId;

    @Column(nullable = false)
    private long itemId;

    @Column(nullable = false)
    private int quantity;

}