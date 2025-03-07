package com.jishop.repository;

import com.jishop.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long> {
}