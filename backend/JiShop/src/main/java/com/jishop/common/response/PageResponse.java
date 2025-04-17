package com.jishop.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PageResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;

    public static <T> PageResponse<T> from(List<T> content, int pageNumber, int pageSize, long totalElements) {
        return new PageResponse<>(
                content,
                pageNumber,
                pageSize,
                (int) Math.ceil((double) totalElements / pageSize),
                totalElements
        );
    }
}
