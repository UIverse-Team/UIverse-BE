package com.example.log.controller;

import com.example.log.dto.LogRequest;
import org.springframework.http.ResponseEntity;

public interface LogController {
    public ResponseEntity addLog(LogRequest logRequest);
}
