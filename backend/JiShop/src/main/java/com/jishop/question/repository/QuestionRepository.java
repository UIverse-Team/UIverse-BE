package com.jishop.question.repository;

import com.jishop.member.domain.User;
import com.jishop.question.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByUserIdOrderByCreatedAt(Long userId);
}
