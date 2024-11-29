package teho.high_traffic_lab.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import teho.high_traffic_lab.entity.User;
import teho.high_traffic_lab.repository.BulkRepository;
import teho.high_traffic_lab.repository.ItemRepository;
import teho.high_traffic_lab.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BulkRepository bulkRepository;

    public void createUser(int count) {
        List<User> users = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= count; i++) {
            String name = "user" + i;
            int age = random.nextInt(70) + 10;
            int level = random.nextInt(5) + 1;
            users.add(new User(i, name, age, level));
        }
        userRepository.saveAll(users);
    }

    public void createUser2(int count) {
        List<User> users = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= count; i++) {
            String name = "user" + i;
            int age = random.nextInt(70) + 10;
            int level = random.nextInt(5) + 1;
            users.add(new User(i, name, age, level));
        }
        bulkRepository.saveAllUsers(users);
    }

//    public void createItem(int count) {
//        List<Item> items = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 1; i <= count; i++) {
//            int price = (random.nextInt(100000) + 1000) / 100 * 100;
//            items.add(new Item("item" + i, price));
//        }
//        itemRepository.saveAll(items);
//    }
//
//    public void createItem2(int count) {
//        List<Item> items = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 1; i <= count; i++) {
//            int price = (random.nextInt(100000) + 1000) / 100 * 100;
//            items.add(new Item("item" + i, price));
//        }
//        bulkRepository.saveAllItems(items);
//    }
}
