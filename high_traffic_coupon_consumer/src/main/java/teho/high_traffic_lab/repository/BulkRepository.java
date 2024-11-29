package teho.high_traffic_lab.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import teho.high_traffic_lab.entity.User;

import java.sql.PreparedStatement;
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
}
