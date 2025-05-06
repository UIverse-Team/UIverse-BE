package com.jiseller.saleproduct.util;

import com.jiseller.option.domain.Option;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record OptionMap(
        Map<Long, Option> optionMap
) {
    public static OptionMap from(List<Option> options) {
        Map<Long, Option> optionMap = options.stream()
                .collect(Collectors.toMap(Option::getId, option -> option));

        return new OptionMap(optionMap);
    }
}
