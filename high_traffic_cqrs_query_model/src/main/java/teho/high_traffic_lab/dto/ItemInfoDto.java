package teho.high_traffic_lab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemInfoDto {
    private String itemName;
    private String categoryName;
    private int price;
    private int quantity;


}
