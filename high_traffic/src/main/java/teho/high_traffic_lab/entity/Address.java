package teho.high_traffic_lab.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long userId;

    private String street;

    private String city;

    private String zipcode;

    public Address(long userId, String street, String city, String zipcode) {
        this.userId = userId;
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
    }
}
