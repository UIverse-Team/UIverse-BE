package com.jishop.product.dto;

public record ProductRequest(
        int page,
        int size,
        String sort

        //TODO
        // 1. filter
){
        public ProductRequest {
                if (page < 0) {page = 0;}
                if (page > 100) {page = 0;}

                if (size < 0) {size = 10;}

                if (sort == null || sort.isEmpty()) {sort = "wish";}
        }
}
