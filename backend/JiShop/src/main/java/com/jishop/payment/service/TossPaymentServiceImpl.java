package com.jishop.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.config.TossPaymentConfig;
import com.jishop.payment.domain.*;
import com.jishop.payment.dto.PaymentConfirmResponse;
import com.jishop.payment.dto.TossConfirmRequest;
import com.jishop.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentServiceImpl implements TossPaymentService {

    private final RestTemplate restTemplate;
    private final TossPaymentConfig tossPaymentConfig;
    private final ObjectMapper objectMapper;
    private final PaymentRepository paymentRepository;

    /**
     * 토스 결제 승인 API 호출 후 Payment 객체를 반환하는 메서드
     *
     * @param request
     * @return
     */
    @Override
    public Payment confirmPayment(TossConfirmRequest request){

        // 결제 승인 요청 데이터 생성
        String jsonBody;
        try{
            jsonBody = objectMapper.writeValueAsString(request);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DomainException(ErrorType.CONFIRM_REQUEST_SERIALIZATION_FAILED);
        }

        // 요청 헤더 설정 - 시크릿 키 인코딩
        HttpHeaders headers = tossPaymentConfig.getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> responseEntity;

        // 토스 결제 승인 API 호출
        try{
            responseEntity = restTemplate.postForEntity(tossPaymentConfig.getConfirmUrl(), httpEntity, String.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DomainException(ErrorType.TOSS_CONFIRM_REQEUST_FAILED);
        }
        if(!responseEntity.getStatusCode().is2xxSuccessful()){
            throw new DomainException(ErrorType.TOSS_CONFIRM_FAILED);
        }

        // 토스 응답 파싱
        String tossResponse = responseEntity.getBody();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(tossResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DomainException(ErrorType.TOSS_RESPONSE_PARSING_FAILED);
        }

        // payment에서 필요한 필드 추출
        JsonNode paymentNode = rootNode.get("payment");
        if (paymentNode == null) {
            throw new DomainException(ErrorType.TOSS_RESPONSE_NO_PAYMENT);
        }

        String paymentKey = paymentNode.get("paymentKey").asText();
        String orderId = paymentNode.get("orderId").asText();
        PaymentStatus status = PaymentStatus.valueOf(paymentNode.get("status").asText());
        LocalDateTime requestedAt = LocalDateTime.parse(paymentNode.get("requestedAt").asText());
        LocalDateTime approvedAt = LocalDateTime.parse(paymentNode.get("approvedAt").asText());
        PaymentMethod method = PaymentMethod.valueOf(paymentNode.get("method").asText());
        String currency = paymentNode.get("currency").asText();
        int totalAmount = paymentNode.get("totalAmount").asInt();
        int suppliedAmount = paymentNode.get("suppliedAmount").asInt();
        int vat = paymentNode.get("vat").asInt();
        int taxFreeAmount = paymentNode.get("taxFreeAmount").asInt();

        //  카드 정보(card), 간편 결제 정보(easyPay) 파싱
        CardInfo cardInfo = null;
        if (rootNode.has("card") && !rootNode.get("card").isNull()) {
            try {
                cardInfo = objectMapper.convertValue(objectMapper.readTree(rootNode.get("card").toString()), CardInfo.class);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        EasyPayInfo easyPayInfo = null;
        if (rootNode.has("easyPay") && !rootNode.get("easyPay").isNull()) {
            try {
                easyPayInfo = objectMapper.convertValue(objectMapper.readTree(rootNode.get("easyPay").toString()), EasyPayInfo.class);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        // Payment 엔티티 생성 및 반환
        return Payment.builder()
                .paymentKey(paymentKey)
                .orderId(orderId)
                .status(status)
                .requestedAt(requestedAt)
                .approvedAt(approvedAt)
                .method(method)
                .currency(currency)
                .totalAmount(totalAmount)
                .suppliedAmount(suppliedAmount)
                .vat(vat)
                .taxFreeAmount(taxFreeAmount)
                .card(cardInfo)
                .easyPay(easyPayInfo)
                .build();
    }
}
