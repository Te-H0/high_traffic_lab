package teho.high_traffic_lab.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetailResponse {

    private long id;
    private String userName;
    private int level;
    private LocalDateTime orderDate;
    private List<OrderItemDetail> itemInfoList;
    private boolean paymentStatus;
    private LocalDateTime paymentDate;

    public OrderDetailResponse(OrderInfoDto orderInfoDto, List<OrderItemDetail> itemInfoList) {
        this.id = orderInfoDto.getId();
        this.userName = orderInfoDto.getUserName();
        this.level = orderInfoDto.getLevel();
        this.orderDate = orderInfoDto.getOrderDate();
        this.itemInfoList = itemInfoList;
        this.paymentStatus = orderInfoDto.isPaymentStatus();
        this.paymentDate = orderInfoDto.getPaymentDate();
    }

    @Data
    @NoArgsConstructor
    public static class OrderItemDetail {
        private String itemName;
        private String categoryName;
        private int price;
        private int quantity;

        public OrderItemDetail(ItemInfoDto itemInfoDto) {
            this.itemName = itemInfoDto.getItemName();
            this.categoryName = itemInfoDto.getCategoryName();
            this.price = itemInfoDto.getPrice();
            this.quantity = itemInfoDto.getQuantity();
        }
    }
}
// order(userID, orderDate) -> user(name, level) -> orderItem(orderId로 조회 itemId,quantity )
//-> item(categoryId, name, price) -> category(name) -> payment(orderId로 조회 status, paymentDate)