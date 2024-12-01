package teho.high_traffic_lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HighTrafficLabCqrsQueryModel {

    public static void main(String[] args) {
        SpringApplication.run(HighTrafficLabCqrsQueryModel.class, args);
    }

}
