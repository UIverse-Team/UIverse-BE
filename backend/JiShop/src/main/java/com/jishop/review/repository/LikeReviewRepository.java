package com.jishop.review.repository;

import com.jishop.member.domain.User;
import com.jishop.review.domain.LikeReview;
import com.jishop.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeReviewRepository extends JpaRepository<LikeReview, Long> {

    @Modifying
    @Query("delete from LikeReview lr where lr.review = :review and lr.user = :user")
    int deleteByReviewAndUser(@Param(value = "review") Review review, @Param(value = "user") User user);
}
