package teho.high_traffic_lab.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teho.high_traffic_lab.entity.*;
import teho.high_traffic_lab.repository.BulkRepository;
import teho.high_traffic_lab.repository.CategoryRepository;
import teho.high_traffic_lab.repository.ItemRepository;
import teho.high_traffic_lab.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonService {
    private static final String[] CATEGORY_LIST = {"음식", "가구", "전자기기", "주방용품", "운동장비", "장난감"};
    private static final int CATEGORY_SIZE = CATEGORY_LIST.length;
    private static final String[] CITY_LIST = {"서울", "인천", "부산", "대구", "제주도", "창원", "김포", "춘천", "평창", "포천", "헬싱키"};
    private static final int CITY_SIZE = CITY_LIST.length;

    private static int ITEM_SIZE = 0;
    private static int USER_SIZE = 0;
    private static int ORDER_SIZE = 0;
    private final int BATCH_SIZE = 500000;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BulkRepository bulkRepository;
    private final CategoryRepository categoryRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(rollbackFor = Exception.class)
    public void initProject(int userCount, int itemCount, int orderCount) {
        initUser(userCount);
        initAddress();
        initCategory();
        initItem(itemCount);
        initOrder(orderCount);
        initPayment();

        System.out.println("초기화 완료~!");
    }

    private void initUser(int userCount) {
        entityManager.createNativeQuery("TRUNCATE TABLE users").executeUpdate();
        List<User> users = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= userCount; i++) {
            String name = "user" + i;
            int age = random.nextInt(70) + 10;
            int level = random.nextInt(5) + 1;
            users.add(new User(name, age, level));
        }
        bulkRepository.saveAllUsers(users);
        USER_SIZE = userCount;
    }

    private void initAddress() {
        entityManager.createNativeQuery("TRUNCATE TABLE address").executeUpdate();
        List<Address> addresses = new ArrayList<>();
        Random random = new Random();
        int size = CITY_LIST.length;
        for (int i = 1; i <= USER_SIZE; i++) {
            // 0부터 categories.length - 1 사이의 랜덤 인덱스 선택
            int idx = random.nextInt(size);
            int streetNum = random.nextInt(100) + 1;
            int zipNum = random.nextInt(8000) + 1000;
            String city = CITY_LIST[idx];
            addresses.add(new Address(i, String.valueOf(streetNum), city, String.valueOf(zipNum)));
        }
        bulkRepository.saveAllAddress(addresses);
    }

    // 카테고리 무조건 6개
    private void initCategory() {
        entityManager.createNativeQuery("TRUNCATE TABLE category").executeUpdate();
        for (String category : CATEGORY_LIST) {
            categoryRepository.save(new Category(category));
        }
    }

    private void initItem(int itemCount) {
        entityManager.createNativeQuery("TRUNCATE TABLE items").executeUpdate();
        List<Item> items = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= itemCount; i++) {
            long categoryId = random.nextLong(CATEGORY_SIZE) + 1;
            String name = "item" + i;
            int price = (random.nextInt(100000) + 1000) / 100 * 100;
            items.add(new Item(categoryId, name, price));
        }
        bulkRepository.saveAllItems(items);
        ITEM_SIZE = itemCount;
    }

    private void initOrder(int orderCount) {
        entityManager.createNativeQuery("TRUNCATE TABLE orders").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE order_items").executeUpdate();
        List<Order> orders = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();

        Random random = new Random();
        for (int i = 1; i <= orderCount; i++) {
            if (i == BATCH_SIZE + 1) {
                bulkRepository.saveAllOrders(orders);
                orders.clear();
            }

            if (orderItems.size() > BATCH_SIZE) {
                bulkRepository.saveAllOrderItems(orderItems);
                orderItems.clear();
            }
            int sameOrderCount = random.nextInt(3) + 1;
            long userId = random.nextLong(USER_SIZE) + 1;
            orders.add(new Order(userId, LocalDateTime.now()));

            Set<Long> itemIds = new HashSet<>();
            for (int j = 0; j < sameOrderCount; j++) {
                long itemId = -1;
                while (true) {
                    itemId = random.nextLong(ITEM_SIZE) + 1;
                    if (!itemIds.contains(itemId)) {
                        itemIds.add(itemId);
                        break;
                    }
                }

                int quantity = random.nextInt(5) + 1;
                orderItems.add(initOderItem(i, itemId, quantity));
            }
        }
        if (!orders.isEmpty()) {
            bulkRepository.saveAllOrders(orders);
        }
        if (!orderItems.isEmpty()) {
            bulkRepository.saveAllOrderItems(orderItems);
        }

        ORDER_SIZE = orderCount;
    }

    private OrderItem initOderItem(int orderId, long itemId, int quantity) {
        return new OrderItem(orderId, itemId, quantity);
    }

    private void initPayment() {
        entityManager.createNativeQuery("TRUNCATE TABLE payment").executeUpdate();
        List<Payment> payments = new ArrayList<>();

        Random random = new Random();
        for (int i = 1; i <= ORDER_SIZE; i++) {
            boolean status = random.nextBoolean();
            LocalDateTime paymentDate = null;
            if (status) {
                // 0부터 180 사이의 랜덤 분 추가 (최대 3시간)
                int randomMinutes = random.nextInt(181);  // 0 ~ 180분
                paymentDate = LocalDateTime.now().plusMinutes(randomMinutes);
            }
            payments.add(new Payment((long) i, status, paymentDate));
        }
        bulkRepository.saveAllPayments(payments);
    }
}
