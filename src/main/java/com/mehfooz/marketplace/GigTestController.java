package com.mehfooz.marketplace;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class GigTestController {

    private final GigRepository gigRepository;
    private final UserRepository userRepository;

    public GigTestController(GigRepository gigRepository, UserRepository userRepository) {
        this.gigRepository = gigRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/test/add-gig")
    public Gig addGig() {
        Gig g = new Gig();
        g.setClient(userRepository.findAll().get(0));
        g.setTitle("Design a logo");
        g.setDescription("Need a simple logo for a college club");
        g.setBudget(new BigDecimal("500"));
        return gigRepository.save(g);
    }

    @GetMapping("/test/gigs")
    public List<Gig> allGigs() {
        return gigRepository.findAll();
    }
}
