package com.jishop.review.controller;

import com.jishop.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class ReviewControllerImpl implements ReviewController{

    private final ReviewService reviewService;


}
