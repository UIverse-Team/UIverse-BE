package com.jishop.controller;

import com.jishop.dto.FaqResponse;

import java.util.List;

public interface FaqController {

    List<FaqResponse> getAllFaqs();
}