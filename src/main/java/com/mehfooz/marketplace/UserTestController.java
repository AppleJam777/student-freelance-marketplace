package com.mehfooz.marketplace;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class UserTestController {

    private final UserRepository userRepository;

    public UserTestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/test/add-user")
    public User addUser() {
        User u = new User();
        u.setName("Test User");
        u.setEmail("test" + System.currentTimeMillis() + "@mail.com");
        u.setPassword("1234");
        u.setRole(Role.FREELANCER);
        return userRepository.save(u);
    }

    @GetMapping("/test/users")
    public List<User> allUsers() {
        return userRepository.findAll();
    }
}
