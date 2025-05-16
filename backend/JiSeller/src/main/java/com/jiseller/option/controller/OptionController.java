package com.jiseller.option.controller;

import com.jiseller.option.dto.RegisterOptionRequest;
import com.jiseller.option.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/options")
public class OptionController {

    private final OptionService optionService;

    @PostMapping("/create")
    public void createOption(@RequestBody RegisterOptionRequest registerOptionRequest) {
        optionService.createOption(registerOptionRequest);
    }
}
