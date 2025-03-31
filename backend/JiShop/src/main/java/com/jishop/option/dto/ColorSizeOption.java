package com.jishop.option.dto;

import java.util.List;

public record ColorSizeOption(
        String color,
        List<SizeOption> sizes
) {
}
