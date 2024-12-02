package teho.high_traffic_lab.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "united_order_info") // 실제 몽고 DB 컬렉션 이름
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {
    @Id
    private String id;

    private String userName;
    private int level;
    private LocalDateTime orderDate;
    private List<ItemInfo> itemInfoList;
    private boolean paymentStatus;
    private LocalDateTime paymentDate;


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemInfo {
        private String itemName;
        private String categoryName;
        private int price;
        private int quantity;
    }
}