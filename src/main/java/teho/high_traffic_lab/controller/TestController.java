package teho.high_traffic_lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teho.high_traffic_lab.entity.Gender;
import teho.high_traffic_lab.service.UserService;
//import teho.high_traffic_lab.dto.UserDto;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final UserService userService;

    @GetMapping("/init")
    public String test(@RequestParam int user, @RequestParam int item) {
        userService.createUser(user);
        userService.createItem(item);
        return "yes~";
    }

    @GetMapping("/init2")
    public String test2(@RequestParam int user, @RequestParam int item) {
        userService.createUser2(user);
        userService.createItem2(item);
        return "yes~";
    }

    @PostMapping("/enum")
    public Gender test2(@RequestBody Gender gender) {
        System.out.println("gender = " + gender);
        return gender;
    }
}
