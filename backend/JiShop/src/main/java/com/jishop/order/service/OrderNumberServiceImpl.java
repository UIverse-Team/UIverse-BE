package com.jishop.order.service;

import com.jishop.order.domain.OrderNumber;
import com.jishop.order.repository.OrderNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrderNumberServiceImpl implements OrderNumberService {

    private final OrderNumberRepository orderNumberRepository;

    private static final int LENGTH = 5;
    private static final String CHARACTERS = "01346789ABCDFGHJKMNPQRSTUVWXYZ";

    public String generateOrderNumber() {
        /*
         * 주문 번호 생성 로직
         * 포맷은 TYYMMDDXXXXXX 이다.
         * T : Type (O : Order)
         * YY : 년도
         * MM : 월
         * DD : 일
         * XXXXX : 5자리 영문자 + 숫자
         */

        String orderTypeCode = "O"; //Order
        //년도와 날짜 정보를 담는다: yymmdd 형식
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = currentDate.format(formatter);

        //5자리 정수를 만든 후, 해당 정수가 존재하는지 확인을 반복한다
        String randomStr = "";
        Random random = new Random();

        do{
            //숫자, 알파벳 대소문자로 이루어진 5자리 랜덤 문자열 생성
            StringBuilder sb = new StringBuilder(LENGTH);
            for(int i = 0; i < LENGTH; i++){
                int randomIndex = random.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                sb.append(randomChar);
                randomStr = sb.toString();
            }
        } while(orderNumberRepository.existsByOrderNumber(orderTypeCode + formattedDate + randomStr));

        OrderNumber orderNumber = OrderNumber.builder()
                        .orderNumber(orderTypeCode + formattedDate + randomStr)
                        .build();
        //주문번호 저장
        orderNumberRepository.save(orderNumber);

        return orderNumber.getOrderNumber();
    }
}

