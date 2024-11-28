package teho.high_traffic_lab.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "items")
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long categoryId;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    public Item(long categoryId, String name, int price) {
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
    }
}
