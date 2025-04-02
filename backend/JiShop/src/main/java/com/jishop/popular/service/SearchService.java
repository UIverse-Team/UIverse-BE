package com.jishop.popular.service;


public interface SearchService {

    boolean processSearch(String keyword, String clientIp);
    boolean isValidKeyword(String keyword);
    boolean isRelatedToSaleProduct(String keyword);
}
