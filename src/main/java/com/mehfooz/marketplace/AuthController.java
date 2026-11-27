package com.mehfooz.marketplace;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam Role role,
                           @RequestParam(required = false) String college,
                           Model model) {

        if (role == Role.ADMIN) {
            model.addAttribute("error", "Invalid role");
            return "register";
        }
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Email already registered");
            return "register";
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setRole(role);
        user.setCollege(college);
        userRepository.save(user);

        return "redirect:/register?success";
    }
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Optional<User> found = userRepository.findByEmail(email);

        if (found.isEmpty() || !encoder.matches(password, found.get().getPassword())) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        User user = found.get();
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("name", user.getName());
        session.setAttribute("role", user.getRole().name());

        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("role", session.getAttribute("role"));
        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
