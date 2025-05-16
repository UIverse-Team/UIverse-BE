package com.jiseller.option.service;

import com.jiseller.option.dto.RegisterOptionRequest;
import com.jiseller.option.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;

    @Override
    @Transactional
    public void createOption(final RegisterOptionRequest registerOptionRequest) {
        optionRepository.save(registerOptionRequest.toEntity());
    }

    public void validateOptionRequest(RegisterOptionRequest registerOptionRequest) {

    }
}
