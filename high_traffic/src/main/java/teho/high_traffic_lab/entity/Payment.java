package teho.high_traffic_lab.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long orderId;

    private boolean status;

    private LocalDateTime paymentDate;

    public Payment(Long orderId, boolean status, LocalDateTime paymentDate) {
        this.orderId = orderId;
        this.status = status;
        this.paymentDate = paymentDate;
    }
}
