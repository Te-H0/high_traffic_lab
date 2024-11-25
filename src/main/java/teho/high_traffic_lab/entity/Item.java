package teho.high_traffic_lab.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "items")
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Order> orders;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    public Item(String name, int price) {
        this.orders = new ArrayList<>();
        this.name = name;
        this.price = price;
    }
}
