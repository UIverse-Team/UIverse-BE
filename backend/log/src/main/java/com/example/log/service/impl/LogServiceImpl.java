package com.example.log.service.impl;

import com.example.log.domain.Log;
import com.example.log.dto.LogRequest;
import com.example.log.repository.LogRepository;
import com.example.log.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    public void addLog(LogRequest logRequest) {
        Log logEntity = logRequest.toEntity();
        logRepository.save(logEntity);
    }
}
