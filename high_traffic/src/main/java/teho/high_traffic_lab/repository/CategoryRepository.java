package teho.high_traffic_lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teho.high_traffic_lab.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
