package com.example.log.controller.impl;

import com.example.log.controller.LogController;
import com.example.log.dto.LogRequest;
import com.example.log.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logs")
public class LogControllerImpl implements LogController {

    private final LogService logService;

    @PostMapping
    public ResponseEntity<Void> addLog(LogRequest logRequest) {

        return ResponseEntity.ok().build();
    }
}
