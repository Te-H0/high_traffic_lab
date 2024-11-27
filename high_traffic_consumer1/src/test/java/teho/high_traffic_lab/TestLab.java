package teho.high_traffic_lab;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import teho.high_traffic_lab.entity.User;
import teho.high_traffic_lab.repository.UserRepository;
import teho.high_traffic_lab.service.UserService;

import java.util.List;

@SpringBootTest
public class TestLab {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

//    @Test
//    @DisplayName("단순 조회 시간 확인")
//    public void findAllUsersTimeTest() {
//        long startTime = System.currentTimeMillis();
//        List<User> all = userRepository.findAll();
//        long endTime = System.currentTimeMillis();
//        System.out.println("조회 시간(ms): " + (endTime - startTime));
//        Assertions.assertThat(all).isNotNull();
//    }

    @Test
    @DisplayName("user level 조건 1개 조회 시간 확인")
    public void findAllUsersByLevelTimeTest() {
        long startTime = System.currentTimeMillis();
        List<User> all = userRepository.findAllByLevel(3);
        long endTime = System.currentTimeMillis();
        System.out.println("조회 시간(ms): " + (endTime - startTime));
        System.out.println("조회된 객체 수:" + all.size());
        Assertions.assertThat(all).isNotNull();
    }

    @Test
    @DisplayName("user age 조건 1개 조회 시간 확인")
    public void findAllUsersByAgeTimeTest() {
        long startTime = System.currentTimeMillis();
        List<User> all = userRepository.findAllByAge(20);
        long endTime = System.currentTimeMillis();
        System.out.println("조회 시간(ms): " + (endTime - startTime));
        System.out.println("조회된 객체 수:" + all.size());
        Assertions.assertThat(all).isNotNull();
    }
}
