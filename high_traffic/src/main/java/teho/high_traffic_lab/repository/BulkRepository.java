package teho.high_traffic_lab.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import teho.high_traffic_lab.entity.*;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAllUsers(List<User> users) {
        String sql = "INSERT INTO users (name, age, level)" +
                "VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                users,
                users.size(),
                (PreparedStatement ps, User user) -> {
                    ps.setString(1, user.getName());
                    ps.setInt(2, user.getAge());
                    ps.setInt(3, user.getLevel());
                }
        );
    }

    @Transactional
    public void saveAllItems(List<Item> items) {
        String sql = "INSERT INTO items (category_id,name, price)" +
                "VALUES (?, ?,?)";

        jdbcTemplate.batchUpdate(sql,
                items,
                items.size(),
                (PreparedStatement ps, Item item) -> {
                    ps.setLong(1, item.getCategoryId());
                    ps.setString(2, item.getName());
                    ps.setInt(3, item.getPrice());
                }
        );
    }

    @Transactional
    public void saveAllAddress(List<Address> addressList) {
        String sql = "INSERT INTO address (user_id, street, city, zipcode)" +
                "VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                addressList,
                addressList.size(),
                (PreparedStatement ps, Address address) -> {
                    ps.setLong(1, address.getUserId());
                    ps.setString(2, address.getStreet());
                    ps.setString(3, address.getCity());
                    ps.setString(4, address.getZipcode());
                }
        );
    }

    @Transactional
    public void saveAllOrders(List<Order> orders) {
        String sql = "INSERT INTO orders (user_id)" +
                "VALUES (?)";

        jdbcTemplate.batchUpdate(sql,
                orders,
                orders.size(),
                (PreparedStatement ps, Order order) -> {
                    ps.setLong(1, order.getUserId());
                }
        );
    }

    @Transactional
    public void saveAllOrderItems(List<OrderItem> orderItems) {
        String sql = "INSERT INTO order_items (order_id,item_id,quantity)" +
                "VALUES (?,?,?)";

        jdbcTemplate.batchUpdate(sql,
                orderItems,
                orderItems.size(),
                (PreparedStatement ps, OrderItem orderItem) -> {
                    ps.setLong(1, orderItem.getOrderId());
                    ps.setLong(2, orderItem.getItemId());
                    ps.setInt(3, orderItem.getQuantity());
                }
        );
    }

    @Transactional
    public void saveAllPayments(List<Payment> payments) {
        String sql = "INSERT INTO payment (order_id,status,payment_date)" +
                "VALUES (?,?,?)";

        jdbcTemplate.batchUpdate(sql,
                payments,
                payments.size(),
                (PreparedStatement ps, Payment payment) -> {
                    ps.setLong(1, payment.getOrderId());
                    ps.setBoolean(2, payment.isStatus());
                    if (payment.getPaymentDate() != null) {
                        ps.setTimestamp(3, Timestamp.valueOf(payment.getPaymentDate()));
                    } else {
                        ps.setNull(3, java.sql.Types.TIMESTAMP);
                    }
                }
        );
    }
}
