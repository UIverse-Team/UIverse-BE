package com.jishop.service;

import com.jishop.dto.FaqResponse;

import java.util.List;

public interface FaqService {

    List<FaqResponse> getAllFaqs();
}