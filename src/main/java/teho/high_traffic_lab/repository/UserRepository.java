package teho.high_traffic_lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teho.high_traffic_lab.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
