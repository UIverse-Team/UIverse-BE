package com.jishop.controller.impl;

import com.jishop.controller.IndexController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexControllerImpl implements IndexController {

    @Override
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
