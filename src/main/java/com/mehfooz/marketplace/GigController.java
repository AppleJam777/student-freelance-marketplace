package com.mehfooz.marketplace;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class GigController {

    private final GigRepository gigRepository;
    private final UserRepository userRepository;

    public GigController(GigRepository gigRepository, UserRepository userRepository) {
        this.gigRepository = gigRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/gigs")
    public String listGigs(@RequestParam(required = false) String keyword,
                           HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        List<Gig> gigs;
        if (keyword != null && !keyword.isBlank()) {
            gigs = gigRepository.findByStatusAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(
                    GigStatus.OPEN, keyword.trim());
        } else {
            gigs = gigRepository.findByStatusOrderByCreatedAtDesc(GigStatus.OPEN);
        }

        model.addAttribute("gigs", gigs);
        model.addAttribute("keyword", keyword);
        return "gigs";
    }

    @GetMapping("/gigs/new")
    public String showNewGigForm(HttpSession session) {
        if (!"CLIENT".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }
        return "new-gig";
    }

    @PostMapping("/gigs")
    public String createGig(@RequestParam String title,
                            @RequestParam String description,
                            @RequestParam BigDecimal budget,
                            HttpSession session) {
        if (!"CLIENT".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        User client = userRepository.findById(userId).orElseThrow();

        Gig gig = new Gig();
        gig.setClient(client);
        gig.setTitle(title);
        gig.setDescription(description);
        gig.setBudget(budget);
        gigRepository.save(gig);

        return "redirect:/gigs";
    }
}