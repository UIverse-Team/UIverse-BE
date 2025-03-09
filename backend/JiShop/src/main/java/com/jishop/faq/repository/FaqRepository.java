package com.jishop.faq.repository;

import com.jishop.faq.domain.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FaqRepository extends JpaRepository<Faq, Long> {
    Page<Faq> findByIsPopularTrue(Pageable pageable);
}