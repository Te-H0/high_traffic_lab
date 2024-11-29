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
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id

    private long id;

    @Column(nullable = false)
    private long categoryId;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

}
