package com.mehfooz.marketplace;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GigRepository extends JpaRepository<Gig, Long> {
    List<Gig> findByStatusOrderByCreatedAtDesc(GigStatus status);

    List<Gig> findByStatusAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(GigStatus status, String keyword);
}