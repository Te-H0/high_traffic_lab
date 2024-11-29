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
    private final int batchSize = 500000;

    @Transactional
    public void saveAllUsers(List<User> users) {
        String sql = "INSERT INTO users (name, age, level)" +
                "VALUES (?, ?, ?)";
        for (int i = 0; i < users.size(); i += batchSize) {
            List<User> batch = users.subList(i, Math.min(i + batchSize, users.size()));
            jdbcTemplate.batchUpdate(sql,
                    batch,
                    batch.size(),
                    (PreparedStatement ps, User user) -> {
                        ps.setString(1, user.getName());
                        ps.setInt(2, user.getAge());
                        ps.setInt(3, user.getLevel());
                    }
            );
        }
    }

    @Transactional
    public void saveAllItems(List<Item> items) {
        String sql = "INSERT INTO items (category_id,name, price)" +
                "VALUES (?, ?,?)";
        for (int i = 0; i < items.size(); i += batchSize) {
            List<Item> batch = items.subList(i, Math.min(i + batchSize, items.size()));
            jdbcTemplate.batchUpdate(sql,
                    batch,
                    batch.size(),
                    (PreparedStatement ps, Item item) -> {
                        ps.setLong(1, item.getCategoryId());
                        ps.setString(2, item.getName());
                        ps.setInt(3, item.getPrice());
                    }
            );
        }
    }

    @Transactional
    public void saveAllAddress(List<Address> addressList) {
        String sql = "INSERT INTO address (user_id, street, city, zipcode)" +
                "VALUES (?, ?, ?, ?)";
        for (int i = 0; i < addressList.size(); i += batchSize) {
            List<Address> batch = addressList.subList(i, Math.min(i + batchSize, addressList.size()));
            jdbcTemplate.batchUpdate(sql,
                    batch,
                    batch.size(),
                    (PreparedStatement ps, Address address) -> {
                        ps.setLong(1, address.getUserId());
                        ps.setString(2, address.getStreet());
                        ps.setString(3, address.getCity());
                        ps.setString(4, address.getZipcode());
                    }
            );
        }
    }

    @Transactional
    public void saveAllOrders(List<Order> orders) {
        String sql = "INSERT INTO orders (user_id, order_date)" +
                "VALUES (?,?)";
        for (int i = 0; i < orders.size(); i += batchSize) {
            List<Order> batch = orders.subList(i, Math.min(i + batchSize, orders.size()));
            jdbcTemplate.batchUpdate(sql,
                    batch,
                    batch.size(),
                    (PreparedStatement ps, Order order) -> {
                        ps.setLong(1, order.getUserId());
                        ps.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
                    }
            );
        }
    }

    @Transactional
    public void saveAllOrderItems(List<OrderItem> orderItems) {
        String sql = "INSERT INTO order_items (order_id,item_id,quantity)" +
                "VALUES (?,?,?)";
        for (int i = 0; i < orderItems.size(); i += batchSize) {
            List<OrderItem> batch = orderItems.subList(i, Math.min(i + batchSize, orderItems.size()));
            jdbcTemplate.batchUpdate(sql,
                    batch,
                    batch.size(),
                    (PreparedStatement ps, OrderItem orderItem) -> {
                        ps.setLong(1, orderItem.getOrderId());
                        ps.setLong(2, orderItem.getItemId());
                        ps.setInt(3, orderItem.getQuantity());
                    }
            );
            batch = null;
        }
    }

    @Transactional
    public void saveAllPayments(List<Payment> payments) {
        String sql = "INSERT INTO payment (order_id,status,payment_date)" +
                "VALUES (?,?,?)";
        for (int i = 0; i < payments.size(); i += batchSize) {
            List<Payment> batch = payments.subList(i, Math.min(i + batchSize, payments.size()));
            jdbcTemplate.batchUpdate(sql,
                    batch,
                    batch.size(),
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
}
