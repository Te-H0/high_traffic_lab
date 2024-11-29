package teho.high_traffic_lab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OrderInfoDto {
    private String userName;
    private int level;
    private LocalDateTime orderDate;
    private boolean paymentStatus;
    private LocalDateTime paymentDate;
}
